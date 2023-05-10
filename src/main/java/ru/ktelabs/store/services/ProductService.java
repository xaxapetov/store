package ru.ktelabs.store.services;

import ru.ktelabs.store.dtos.responses.ProductInformationResponse;
import ru.ktelabs.store.dtos.responses.ProductResponse;
import ru.ktelabs.store.exeptions.ClientNotFoundException;
import ru.ktelabs.store.exeptions.ProductNotFoundException;
import ru.ktelabs.store.models.Product;

import java.util.List;

public interface ProductService {

    List<ProductResponse> getAllProducts();

    ProductInformationResponse getProductInfo(Long productId, Long clientId) throws ProductNotFoundException;

    void setRatingForProduct(Long productId, Long clientId, Integer rating)
            throws ProductNotFoundException, ClientNotFoundException;

    Product getProductById(Long id) throws ProductNotFoundException;
}
