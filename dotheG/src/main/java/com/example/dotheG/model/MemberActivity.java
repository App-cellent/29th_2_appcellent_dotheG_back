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
public class MemberActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ACTIVITY_ID")
    private Long userAcvityId;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private Member userId;

    @ManyToOne
    @JoinColumn(name = "ACTICITY_ID")
    private Activity activityId;

    private String activityName;

    // Todo : 이미지
    @Lob
    private byte[] activityImage;

    private LocalDate activityDate;
}
