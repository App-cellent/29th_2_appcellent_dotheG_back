package com.example.dotheG.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "week_report")
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

    private LocalDate weekStartDate;

    private LocalDate weekEndDate;

}
