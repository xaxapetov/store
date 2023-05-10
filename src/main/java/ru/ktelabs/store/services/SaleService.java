package ru.ktelabs.store.services;

import ru.ktelabs.store.dtos.requests.RegisterSaleRequest;
import ru.ktelabs.store.dtos.requests.TotalCostRequest;
import ru.ktelabs.store.dtos.responses.RegisterSaleResponse;
import ru.ktelabs.store.dtos.responses.TotalCostResponse;
import ru.ktelabs.store.exeptions.ClientNotFoundException;
import ru.ktelabs.store.exeptions.ProductNotFoundException;
import ru.ktelabs.store.exeptions.TotalCostDoesNotMatch;

public interface SaleService {

    TotalCostResponse getTotalCost(TotalCostRequest request) throws ClientNotFoundException, ProductNotFoundException;

    RegisterSaleResponse  registerSale(RegisterSaleRequest request) throws ClientNotFoundException, ProductNotFoundException, TotalCostDoesNotMatch;
}
