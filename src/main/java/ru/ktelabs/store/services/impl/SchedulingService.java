package ru.ktelabs.store.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.ktelabs.store.models.GlobalDiscount;
import ru.ktelabs.store.models.Product;
import ru.ktelabs.store.repositories.GlobalDiscountRepository;
import ru.ktelabs.store.repositories.ProductRepository;
import ru.ktelabs.store.services.StatisticService;

import java.time.LocalDateTime;

import static ru.ktelabs.store.services.impl.SaleServiceImpl.MAX_DISCOUNT;

@EnableAsync
@Service
@RequiredArgsConstructor
public class SchedulingService {

    private final ProductRepository productRepository;
    private final GlobalDiscountRepository discountRepository;
    private final StatisticService statisticService;

    @Async
    @Scheduled(cron = "0 0 */1 * * *")
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void scheduleSetRandomDiscountTaskAsync() {
        Product product = productRepository.getRandomProduct().orElse(null);
        if (product == null) {
            return;
        }
        LocalDateTime localDateTime = LocalDateTime.now();
        GlobalDiscount globalDiscount = discountRepository.findByProduct(product).orElseGet(GlobalDiscount::new);
        globalDiscount.setProduct(product);
        globalDiscount.setStartDate(localDateTime);
        globalDiscount.setEndDate(localDateTime.plusHours(1));
        globalDiscount.setValue((int) (Math.random() * MAX_DISCOUNT + 1));
        discountRepository.save(globalDiscount);
    }

    @Async
    @Scheduled(cron = "0 0 */1 * * *")
    public void scheduleCollectStatisticTaskAsync() {
        statisticService.setStatisticClient();
        statisticService.setStatisticProduct();
    }
}