package ru.ktelabs.store.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.ktelabs.store.dtos.responses.ProductInformationResponse;
import ru.ktelabs.store.dtos.responses.ProductResponse;
import ru.ktelabs.store.exeptions.ClientNotFoundException;
import ru.ktelabs.store.exeptions.ProductNotFoundException;
import ru.ktelabs.store.services.ProductService;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/store/products")
@RequiredArgsConstructor
@Validated
public class ProductController {

    private final ProductService productService;

    /**
     * Возвращает список доступных товаров
     *
     * @return список товаров (идентификатор, наименование, цена)
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getProducts() {
        return productService.getAllProducts();
    }

    /**
     * Получение информации по конкретному товару
     *
     * @param productId идентификатор товара
     * @param clientId  идентификатор клиента*
     * @return вых. параметры:
     * - описание;
     * - средняя оценка (с точностью до 1 десятичного знака);
     * - распределение оценок (от 1 до 5, парами "оценка - количество");
     * - текущая оценка товара клиентом.
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductInformationResponse getProductInfo(@PathVariable("id") Long productId,
                                                     @RequestParam("client-id") Long clientId) throws ProductNotFoundException {
        return productService.getProductInfo(productId, clientId);
    }

    /**
     * Поставить оценку товару
     *
     * @param productId идентификатор товара
     * @param clientId  идентификатор клиента
     * @param rating    оценка (1-5 или null для отзыва оценки)
     */
    @PostMapping("/{id}/rating")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void setRating(@PathVariable("id") Long productId,
                          @RequestParam(name = "client-id") Long clientId,
                          @RequestParam(name = "rating", required = false) @Min(1) @Max(5) Integer rating)
            throws ProductNotFoundException, ClientNotFoundException {
        productService.setRatingForProduct(productId, clientId, rating);
    }
}
