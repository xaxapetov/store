package ru.ktelabs.store.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.ktelabs.store.dtos.responses.ClientResponse;
import ru.ktelabs.store.dtos.requests.ChangeDiscountsRequest;
import ru.ktelabs.store.exeptions.ClientNotFoundException;
import ru.ktelabs.store.services.ClientService;

import java.util.List;

@RestController
@RequestMapping("/store/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    /**
     * Получить список клиентов (все атрибуты)
     *
     * @return список клиентов
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ClientResponse> getClients() {
        return clientService.getAllClients();
    }

    /**
     * Изменение индивидуальных скидок клиента
     *
     * @param id      (идентификатор клиента)
     * @param request (скидка 1 и скидка 2)
     * @throws ClientNotFoundException
     */
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeDiscounts(@PathVariable("id") Long id,
                                @RequestBody ChangeDiscountsRequest request) throws ClientNotFoundException {
        clientService.changeDiscountsForClient(id, request);
    }
}
