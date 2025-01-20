package com.example.dotheG.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.sql.Timestamp;

@Entity
@Getter
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

    //private String content;

    private boolean isRead;

    private Timestamp sendTime;

    public void updateIsRead() {
        this.isRead = true;
    }

}
