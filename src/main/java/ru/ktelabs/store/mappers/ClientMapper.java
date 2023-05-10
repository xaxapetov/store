package ru.ktelabs.store.mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.ktelabs.store.dtos.requests.ChangeDiscountsRequest;
import ru.ktelabs.store.dtos.responses.ClientResponse;
import ru.ktelabs.store.models.Client;

@Mapper(componentModel = "spring")
public interface ClientMapper {
    ClientResponse clientToClientResponse(Client client);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateClientFromChangeDiscountsRequest(ChangeDiscountsRequest request, @MappingTarget Client client);
}
