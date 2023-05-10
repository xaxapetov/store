package ru.ktelabs.store.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "product_statistic")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatisticProduct {
    @Id
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "cheque_count")
    private Integer chequeCount;

    @Column(name = "total_sum")
    private BigDecimal totalSum;

    @Column(name = "discount_sum")
    private BigDecimal discountSum;
}
