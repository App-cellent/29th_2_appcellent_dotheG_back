package com.example.dotheG.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "carbon_ranking")
public class CarbonRanking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CARBON_RANKING_ID")
    private Long carbonRankingId;

    @ManyToOne
    @JoinColumn(name = "MONTH_REPORT_ID")
    private MonthReport monthReportId;

    private String range;

    private int userCount;
}