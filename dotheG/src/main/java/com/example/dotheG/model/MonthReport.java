package com.example.dotheG.model;

import jakarta.persistence.*;

import java.security.Timestamp;
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

    @Column(name = "report_month") // 변경됨
    private Timestamp reportMonth;

    private int monthlyAvgSteps;

    private int monthlyCertification;

    private int monthlyReward;

    //TODO 랭킹 순위 변수 지정
}