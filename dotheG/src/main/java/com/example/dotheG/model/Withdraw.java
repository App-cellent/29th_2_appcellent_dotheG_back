package com.example.dotheG.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Withdraw {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "WITHDRAWAL_ID")
    private Long withdrawalId;

    @OneToOne
    @JoinColumn(name = "USER_ID")
    private Member userId;

    private String withdrawalReason;

    public Withdraw(Member userId, String withdrawalReason) {
        this.userId = userId;
        this.withdrawalReason = withdrawalReason;
    }
}
