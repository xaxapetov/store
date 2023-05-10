package ru.ktelabs.store.dtos.responses;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductResponse {

    private Long id;

    private String name;

    private BigDecimal price;
}
