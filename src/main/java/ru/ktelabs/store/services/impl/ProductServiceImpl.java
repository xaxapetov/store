package ru.ktelabs.store.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.ktelabs.store.dtos.responses.ProductInformationResponse;
import ru.ktelabs.store.dtos.responses.ProductResponse;
import ru.ktelabs.store.exeptions.ClientNotFoundException;
import ru.ktelabs.store.exeptions.ProductNotFoundException;
import ru.ktelabs.store.mappers.ProductMapper;
import ru.ktelabs.store.models.Product;
import ru.ktelabs.store.models.Rating;
import ru.ktelabs.store.repositories.ClientRepository;
import ru.ktelabs.store.repositories.ProductRepository;
import ru.ktelabs.store.repositories.RatingRepository;
import ru.ktelabs.store.services.ProductService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ClientRepository clientRepository;
    private final ProductMapper productMapper;
    private final RatingRepository ratingRepository;


    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();

        return products.stream().map(productMapper::productToProductResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public ProductInformationResponse getProductInfo(Long productId, Long clientId) throws ProductNotFoundException {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product with id = " + productId + " not found"));
        Rating rating = null;
        if (clientId != null) {
            rating = product.getRatingsList().stream()
                    .filter(x -> clientId.equals(x.getClientId()))
                    .findFirst()
                    .orElse(null);
        }

        return productMapper.productToProductInformationResponse(product, rating);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void setRatingForProduct(Long productId, Long clientId, Integer rating)
            throws ProductNotFoundException, ClientNotFoundException {
        clientRepository.findById(clientId)
                .orElseThrow(() -> new ClientNotFoundException("Client with id = " + clientId + " not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product with id = " + productId + " not found"));

        Rating ratingFromProduct = product.getRatingsList().stream()
                .filter(x -> clientId.equals(x.getClientId()))
                .findFirst()
                .orElseGet(() -> Rating.builder()
                        .product(product)
                        .clientId(clientId)
                        .build());
        ratingFromProduct.setRatingValue(rating);
        ratingRepository.save(ratingFromProduct);
    }

    @Override
    public Product getProductById(Long id) throws ProductNotFoundException {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with id = " + id + " not found"));
    }
}