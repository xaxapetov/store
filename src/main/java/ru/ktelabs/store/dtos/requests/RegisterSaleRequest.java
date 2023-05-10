package ru.ktelabs.store.dtos.requests;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Map;

@Getter
@Setter
public class RegisterSaleRequest {

    @NotNull
    private Long clientId;
    @NotNull
    private Integer totalCost;
    @NotNull
    private Map<Long, Integer> productsCount;
}