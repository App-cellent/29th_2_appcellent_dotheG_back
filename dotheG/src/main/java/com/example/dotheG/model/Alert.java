package com.example.dotheG.model;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ALERT_ID")
    private Long alertId;

    @Enumerated(EnumType.STRING)
    private ALERTYPE alertType;

    private String alertContent;

    public enum ALERTYPE {
        REPORT,
        STEPGOAL,
        QUESTPENDING,
        QUIZPENDING,
    }
}
