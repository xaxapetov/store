package ru.ktelabs.store.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.ktelabs.store.dtos.responses.StatisticResponse;
import ru.ktelabs.store.exeptions.MoreThanOneArgumentException;
import ru.ktelabs.store.exeptions.StatisticNotFoundException;
import ru.ktelabs.store.mappers.StatisticMapper;
import ru.ktelabs.store.models.Client;
import ru.ktelabs.store.models.Position;
import ru.ktelabs.store.models.Product;
import ru.ktelabs.store.models.Sale;
import ru.ktelabs.store.models.StatisticClient;
import ru.ktelabs.store.models.StatisticProduct;
import ru.ktelabs.store.repositories.ClientRepository;
import ru.ktelabs.store.repositories.ProductRepository;
import ru.ktelabs.store.repositories.SaleRepository;
import ru.ktelabs.store.repositories.StatisticClientRepository;
import ru.ktelabs.store.repositories.StatisticProductRepository;
import ru.ktelabs.store.services.StatisticService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {

    private final StatisticProductRepository statisticProductRepository;
    private final StatisticClientRepository statisticClientRepository;
    private final StatisticMapper statisticMapper;
    private final ProductRepository productRepository;
    private final ClientRepository clientRepository;
    private final SaleRepository saleRepository;


    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public StatisticResponse getStatistic(Long clientId, Long productId)
            throws MoreThanOneArgumentException, StatisticNotFoundException {

        if (clientId == null && productId == null || clientId != null && productId != null) {
            throw new MoreThanOneArgumentException("More than one argument");
        }
        StatisticResponse statisticResponse;
        if (clientId != null) {
            StatisticClient client = statisticClientRepository.findById(clientId)
                    .orElseThrow(() -> new StatisticNotFoundException("Statistic not found"));
            statisticResponse = statisticMapper.statisticClientToStatisticResponse(client);
        } else {
            StatisticProduct statisticProduct = statisticProductRepository.findById(productId)
                    .orElseThrow(() -> new StatisticNotFoundException("Statistic not found"));
            statisticResponse = statisticMapper.statisticProductToStatisticResponse(statisticProduct);
        }
        return statisticResponse;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void setStatisticClient() {
        int chequeCount = 0;
        BigDecimal totalSum;
        BigDecimal discountSum;
        List<Client> clientsList = clientRepository.findAll();
        List<StatisticClient> statisticClients = new ArrayList<>();
        for (Client client : clientsList) {
            List<Sale> saleList = saleRepository.findAllByClient(client);
            chequeCount = saleList.size();
            List<Position> positionList = saleList.stream().flatMap(sale -> sale.getPositions().stream())
                    .collect(Collectors.toList());
            totalSum = positionList.stream().map(Position::getInitialPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal finalSum = positionList.stream().map(Position::getFinalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
            discountSum = totalSum.subtract(finalSum);
            statisticClients.add(StatisticClient.builder()
                    .clientId(client.getId())
                    .chequeCount(chequeCount)
                    .totalSum(totalSum)
                    .discountSum(discountSum)
                    .build());
        }
        statisticClientRepository.saveAll(statisticClients);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void setStatisticProduct() {
        int chequeCount = 0;
        BigDecimal totalSum;
        BigDecimal discountSum;
        List<Product> productsList = productRepository.findAll();
        List<StatisticProduct> statisticProducts = new ArrayList<>();
        for (Product product : productsList) {
            List<Position> positionList = product.getPositionsList();
            chequeCount = positionList.size();
            totalSum = positionList.stream().map(Position::getInitialPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal finalSum = positionList.stream().map(Position::getFinalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
            discountSum = totalSum.subtract(finalSum);
            statisticProducts.add(StatisticProduct.builder()
                    .productId(product.getId())
                    .chequeCount(chequeCount)
                    .totalSum(totalSum)
                    .discountSum(discountSum)
                    .build());
        }
        statisticProductRepository.saveAll(statisticProducts);
    }
}
