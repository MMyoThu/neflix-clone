package com.mta.core.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "user_fresh_token")
public class UserRefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "reg_dt")
    private LocalDateTime regDt;

    @Column(name = "reg_id")
    private Long regId;

    @Column(name = "device_id", length = 255)
    private String deviceId;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    @Column(name = "refresh_token", nullable = false, length = 255)
    private String refreshToken;

    @Column(name = "chg_dt")
    private LocalDateTime chgDt;

    @Column(name = "chg_id")
    private Long chgId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_type", length = 255)
    private String userType;
}
