package ru.ktelabs.store.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.ktelabs.store.dtos.requests.RegisterSaleRequest;
import ru.ktelabs.store.dtos.requests.TotalCostRequest;
import ru.ktelabs.store.dtos.responses.RegisterSaleResponse;
import ru.ktelabs.store.dtos.responses.TotalCostResponse;
import ru.ktelabs.store.exeptions.ClientNotFoundException;
import ru.ktelabs.store.exeptions.ProductNotFoundException;
import ru.ktelabs.store.exeptions.TotalCostDoesNotMatch;
import ru.ktelabs.store.models.Client;
import ru.ktelabs.store.models.GlobalDiscount;
import ru.ktelabs.store.models.Position;
import ru.ktelabs.store.models.Product;
import ru.ktelabs.store.models.Sale;
import ru.ktelabs.store.repositories.SaleRepository;
import ru.ktelabs.store.services.ClientService;
import ru.ktelabs.store.services.ProductService;
import ru.ktelabs.store.services.SaleService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SaleServiceImpl implements SaleService {

    public static final int MAX_DISCOUNT = 18;
    private static final String CHEQUE_PATTERN = "00000";
    private static final int QUANTITY_FOR_SECOND_DISCOUNT = 5;
    private final ClientService clientService;
    private final ProductService productService;
    private final SaleRepository saleRepository;

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public TotalCostResponse getTotalCost(TotalCostRequest request) throws ClientNotFoundException, ProductNotFoundException {
        int amountResult = 0;
        Client client = clientService.getClientById(request.getClientId());

        Map<Long, Integer> allProductsForSale = request.getProductsCount();
        for (Long productId : allProductsForSale.keySet()) {
            int quantity = allProductsForSale.get(productId) == null ? 0 : allProductsForSale.get(productId);
            Product product = productService.getProductById(productId);
            int discountResult = getDiscountCoefficientForProduct(client, product.getDiscount(), quantity);
            BigDecimal price = product.getPrice();
            amountResult += setAmount(price, quantity, discountResult).doubleValue() * 100;
        }
        TotalCostResponse response = new TotalCostResponse();
        response.setTotalCost(amountResult);
        return response;
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public RegisterSaleResponse registerSale(RegisterSaleRequest request)
            throws ClientNotFoundException, ProductNotFoundException, TotalCostDoesNotMatch {
        int amountResult = 0;
        Client client = clientService.getClientById(request.getClientId());
        List<Position> positionList = new ArrayList<>();
        Map<Long, Integer> allProductsForSale = request.getProductsCount();
        Sale sale = new Sale();

        for (Long productId : allProductsForSale.keySet()) {
            int quantity = allProductsForSale.get(productId) == null ? 0 : allProductsForSale.get(productId);
            Product product = productService.getProductById(productId);
            Position position = createPosition(client, product, quantity, sale);
            positionList.add(position);
            amountResult += position.getFinalPrice().doubleValue() * 100;
        }
        if (amountResult != request.getTotalCost()) {
            throw new TotalCostDoesNotMatch("Different total cost");
        }
        LocalDateTime localDate = LocalDateTime.now();
        int numberOfCheque = createCheque(localDate.toLocalDate());
        registerFullSale(client, localDate, numberOfCheque, positionList, sale);
        RegisterSaleResponse response = new RegisterSaleResponse();
        response.setCheque(formatChequeToString(numberOfCheque));
        return response;
    }

    private void registerFullSale(Client client, LocalDateTime localDate, int numberOfCheque, List<Position> positionList, Sale sale) {
        sale.setClient(client);
        sale.setDateOfSale(localDate);
        sale.setCheque(numberOfCheque);
        sale.setPositions(positionList);
        saleRepository.save(sale);
    }

    private String formatChequeToString(int cheque) {
        StringBuilder str = new StringBuilder();
        str.append(CHEQUE_PATTERN);
        str.append(cheque);
        str.substring(CHEQUE_PATTERN.length());
        return str.toString();
    }

    private int createCheque(LocalDate localDate) {
        Sale saleByMaxCheque = saleRepository.findByDateOrderByChequeDesc(localDate).orElse(null);
        if (saleByMaxCheque == null || saleByMaxCheque.getCheque() == null) {
            return 100;
        }
        return saleByMaxCheque.getCheque() + 1;
    }

    private Position createPosition(Client client, Product product, int quantity, Sale sale) {
        int discountResult = getDiscountCoefficientForProduct(client, product.getDiscount(), quantity);
        BigDecimal initialPrice = setAmount(product.getPrice(), quantity, 0);
        BigDecimal finalPrice = setAmount(product.getPrice(), quantity, discountResult);
        return Position.builder()
                .product(product)
                .quantity(quantity)
                .sale(sale)
                .initialPrice(initialPrice)
                .finalPrice(finalPrice)
                .finalDiscount(discountResult)
                .build();
    }

    private BigDecimal setAmount(BigDecimal price, int quantity, int discountResult) {
        double discountCoef = ((100 - discountResult) / 100.0);
        return price.multiply(BigDecimal.valueOf(quantity * discountCoef));
    }

    private Integer getDiscountCoefficientForProduct(Client client, GlobalDiscount productDiscount, int quantity) {
        Integer discount1 = client.getDiscount1();
        Integer discount2 = client.getDiscount2();
        int sumDiscount = getGlobalDiscountValueIfExists(productDiscount);
        if (discount2 != null && quantity >= QUANTITY_FOR_SECOND_DISCOUNT) {
            sumDiscount += discount2;
        } else if (discount2 != null) {
            sumDiscount += discount1;
        }
        return Math.min(sumDiscount, MAX_DISCOUNT);
    }

    private int getGlobalDiscountValueIfExists(GlobalDiscount globalDiscount) {
        if (globalDiscount == null || globalDiscount.getValue() == null) {
            return 0;
        }
        LocalDateTime localTime = LocalDateTime.now();
        LocalDateTime startTime = globalDiscount.getStartDate();
        LocalDateTime endTime = globalDiscount.getEndDate();
        if (localTime.isAfter(startTime) && localTime.isBefore(endTime)) {
            return globalDiscount.getValue();
        }
        return 0;
    }
}
