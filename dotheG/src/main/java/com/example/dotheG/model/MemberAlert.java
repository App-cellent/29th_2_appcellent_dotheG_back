package com.example.dotheG.model;

import jakarta.persistence.*;

@Entity
public class MemberAlert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ALERT_ID")
    private Long userAlertId;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private Member userId;

    @ManyToOne
    @JoinColumn(name = "ALERT_ID")
    private Alert alertId;

    private boolean isRead;

}
