package ru.ktelabs.store.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.ktelabs.store.dtos.requests.ChangeDiscountsRequest;
import ru.ktelabs.store.dtos.responses.ClientResponse;
import ru.ktelabs.store.exeptions.ClientNotFoundException;
import ru.ktelabs.store.mappers.ClientMapper;
import ru.ktelabs.store.models.Client;
import ru.ktelabs.store.repositories.ClientRepository;
import ru.ktelabs.store.services.ClientService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientsServiceImpl implements ClientService {

    private final ClientMapper clientMapper;
    private final ClientRepository clientRepository;


    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<ClientResponse> getAllClients() {
        List<Client> clients = clientRepository.findAll();

        return clients.stream().map(clientMapper::clientToClientResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void changeDiscountsForClient(Long id, ChangeDiscountsRequest request) throws ClientNotFoundException {
        Client client = clientRepository.findClientById(id)
                .orElseThrow(() -> new ClientNotFoundException("Client with id = " + id + " not found"));
        clientMapper.updateClientFromChangeDiscountsRequest(request, client);
        clientRepository.save(client);
    }

    @Override
    @Transactional(readOnly = true)
    public Client getClientById(Long id) throws ClientNotFoundException {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Client with id = " + id + " not found"));
    }
}
