package ru.ktelabs.store.dtos.responses.errors;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {

    private String reasonPhrase;

    private int status;

    private String errorMessage;
}