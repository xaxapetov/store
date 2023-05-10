package ru.ktelabs.store.dtos.responses;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class ProductInformationResponse {

    private String description;

    private String averageRating;

    private Map<Integer, Long> ratings;

    private Integer clientRating;
}