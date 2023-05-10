package ru.ktelabs.store.dtos.responses;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class StatisticResponse {

    private Integer chequeCount;

    private BigDecimal totalSum;

    private BigDecimal discountSum;
}
