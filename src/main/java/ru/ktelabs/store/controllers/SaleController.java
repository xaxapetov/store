package ru.ktelabs.store.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.ktelabs.store.dtos.requests.RegisterSaleRequest;
import ru.ktelabs.store.dtos.requests.TotalCostRequest;
import ru.ktelabs.store.dtos.responses.RegisterSaleResponse;
import ru.ktelabs.store.dtos.responses.TotalCostResponse;
import ru.ktelabs.store.exeptions.ClientNotFoundException;
import ru.ktelabs.store.exeptions.ProductNotFoundException;
import ru.ktelabs.store.exeptions.TotalCostDoesNotMatch;
import ru.ktelabs.store.services.SaleService;

@RestController
@RequestMapping("/store/sales")
@RequiredArgsConstructor
public class SaleController {

    private final SaleService saleService;

    /**
     * Запрос итоговой стоимости
     *
     * @param request идентификатор клиента*
     *                - парами: идентификатор товара,  количество.
     * @return итоговая стоимость с учетом скидок (в копейках).
     */
    @PostMapping("/total-cost")
    @ResponseStatus(HttpStatus.OK)
    public TotalCostResponse getTotalCost(@RequestBody @Validated TotalCostRequest request)
            throws ClientNotFoundException, ProductNotFoundException {
        return saleService.getTotalCost(request);
    }

    /**
     * Регистрация продажи
     *
     * @param request парами: идентификатор товара, количество;
     *                - итоговая стоимость с учетом скидок (в копейках).
     * @return - номер чека.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterSaleResponse registerSale(@RequestBody @Validated RegisterSaleRequest request)
            throws ProductNotFoundException, ClientNotFoundException, TotalCostDoesNotMatch {

        return saleService.registerSale(request);
    }
}
