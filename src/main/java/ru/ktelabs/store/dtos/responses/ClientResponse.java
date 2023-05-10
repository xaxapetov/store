package ru.ktelabs.store.dtos.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientResponse {

    private Long id;

    private String name;

    private Integer discount1;

    private Integer discount2;
}
