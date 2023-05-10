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
@Table(name = "client_statistic")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatisticClient {
    @Id
    @Column(name = "client_id")
    private Long clientId;

    @Column(name = "cheque_count")
    private Integer chequeCount;

    @Column(name = "total_sum")
    private BigDecimal totalSum;

    @Column(name = "discount_sum")
    private BigDecimal discountSum;
}
