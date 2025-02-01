package com.example.dotheG.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class MemberAlert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ALERT_ID")
    private Long userAlertId;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private Member userId;

    private String title;

    private String content;

    private boolean isRead;

    private LocalDateTime sendTime;

    public void updateIsRead() {
        this.isRead = true;
    }

    public MemberAlert(Member member, String title, String content, LocalDateTime sendTime) {
        this.userId = member;
        this.title = title;
        this.content = content;
        this.isRead = false;
        this.sendTime = sendTime;

    }

}
