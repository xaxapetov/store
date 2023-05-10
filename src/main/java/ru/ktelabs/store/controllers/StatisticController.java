package ru.ktelabs.store.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.ktelabs.store.dtos.responses.StatisticResponse;
import ru.ktelabs.store.exeptions.MoreThanOneArgumentException;
import ru.ktelabs.store.exeptions.StatisticNotFoundException;
import ru.ktelabs.store.services.StatisticService;

@RestController
@RequestMapping("/store/statistic")
@RequiredArgsConstructor
public class StatisticController {

    private final StatisticService statisticService;

    /**
     * Получение статистики
     *
     * @param clientId  идентификатор клиента;
     * @param productId идентификатор товара;
     * @return вых. параметры:
     * - кол-во чеков;
     * - общая стоимость (для клиента - чеков, для товаров - соотв. позиций) по исходной цене;
     * - сумма скидок (для клиента - по всем позициям чеков, для товаров - соотв. позиций).
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public StatisticResponse getStatistic(@RequestParam(value = "client_id", required = false) Long clientId,
                                          @RequestParam(value = "product_id", required = false) Long productId)
            throws MoreThanOneArgumentException, StatisticNotFoundException {
        return statisticService.getStatistic(clientId, productId);
    }
}
