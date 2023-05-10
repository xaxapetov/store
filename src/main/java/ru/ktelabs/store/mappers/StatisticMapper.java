package ru.ktelabs.store.mappers;


import org.mapstruct.Mapper;
import ru.ktelabs.store.dtos.responses.StatisticResponse;
import ru.ktelabs.store.models.StatisticClient;
import ru.ktelabs.store.models.StatisticProduct;

@Mapper(componentModel = "spring")
public interface StatisticMapper {

    StatisticResponse statisticClientToStatisticResponse(StatisticClient client);

    StatisticResponse statisticProductToStatisticResponse(StatisticProduct client);
}
