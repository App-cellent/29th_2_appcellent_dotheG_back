package com.example.dotheG.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class MonthReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MONTH_REPORT_ID")
    private Long monthReportId;

    @OneToOne
    @JoinColumn(name = "USER_ID")
    private Member userId;

    private Date month;

    private int monthlyAvgSteps;

    private int monthlyCertification;

    private int monthlyReward;

    //TODO 랭킹 순위 변수 지정
}
