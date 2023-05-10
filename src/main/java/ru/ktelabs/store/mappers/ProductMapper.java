package ru.ktelabs.store.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.ktelabs.store.dtos.responses.ProductInformationResponse;
import ru.ktelabs.store.dtos.responses.ProductResponse;
import ru.ktelabs.store.models.Product;
import ru.ktelabs.store.models.Rating;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class ProductMapper {
    public abstract ProductResponse productToProductResponse(Product product);

    @Mapping(source = "product.description", target = "description")
    @Mapping(source = "product.averageRating", target = "averageRating", qualifiedByName = "averageRatingFromDoubleToString")
    @Mapping(source = "product.ratingsList", target = "ratings", qualifiedByName = "ratingsFromListToMap")
    @Mapping(source = "rating.ratingValue", target = "clientRating")
    public abstract ProductInformationResponse productToProductInformationResponse(Product product, Rating rating);

    @Named("averageRatingFromDoubleToString")
    protected String averageRatingFromDoubleToString(Double averageRating) {
        return String.format("%.1f", averageRating);
    }

    @Named("ratingsFromListToMap")
    protected Map<Integer, Long> ratingsFromListToMap(List<Rating> ratingsList) {
        return ratingsList.stream().filter(x -> x.getRatingValue() != null)
                .collect(Collectors.groupingBy(Rating::getRatingValue, Collectors.counting()));
    }
}