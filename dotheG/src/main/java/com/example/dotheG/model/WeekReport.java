package com.example.dotheG.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class WeekReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "WEEK_REPORT_ID")
    private Long weekReportId;

    @OneToOne
    @JoinColumn(name = "USER_ID")
    private Member userId;

    private int weeklyCertification;

    private int weeklyAvgSteps;

    private Date weekStartDate;

    private Date weekEndDate;

}
