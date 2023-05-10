package ru.ktelabs.store.services;

import ru.ktelabs.store.dtos.requests.ChangeDiscountsRequest;
import ru.ktelabs.store.dtos.responses.ClientResponse;
import ru.ktelabs.store.exeptions.ClientNotFoundException;
import ru.ktelabs.store.models.Client;

import java.util.List;

public interface ClientService {

    List<ClientResponse> getAllClients();

    void changeDiscountsForClient(Long id, ChangeDiscountsRequest request) throws ClientNotFoundException;

    Client getClientById(Long id) throws ClientNotFoundException;
}
