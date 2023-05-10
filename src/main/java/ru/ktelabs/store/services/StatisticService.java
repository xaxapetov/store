package ru.ktelabs.store.services;

import ru.ktelabs.store.dtos.responses.StatisticResponse;
import ru.ktelabs.store.exeptions.MoreThanOneArgumentException;
import ru.ktelabs.store.exeptions.StatisticNotFoundException;

public interface StatisticService {

    StatisticResponse getStatistic(Long clientId, Long productId) throws MoreThanOneArgumentException, StatisticNotFoundException;

    void setStatisticProduct();

    void setStatisticClient();
}
