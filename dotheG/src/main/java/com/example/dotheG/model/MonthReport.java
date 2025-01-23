package com.example.dotheG.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MonthReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MONTH_REPORT_ID")
    private Long monthReportId;

    @OneToOne
    @JoinColumn(name = "USER_ID")
    private Member userId;

    @Column(name = "REPORT_MONTH")
    private LocalDate reportMonth;

    private int monthlyTotalSteps;

    private int monthlyTotalCertifications;

    public MonthReport(Member userId, LocalDate reportMonth, int monthlyTotalSteps, int monthlyTotalCertifications) {
        this.userId = userId;
        this.reportMonth = reportMonth;
        this.monthlyTotalSteps = monthlyTotalSteps;
        this.monthlyTotalCertifications = monthlyTotalCertifications;
    }
}