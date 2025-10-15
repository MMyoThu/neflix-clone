package com.mta.core.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "user_token")
public class UserToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "reg_dt", nullable = false)
    private LocalDateTime regDt;

    @Column(name = "expire_format", length = 8)
    private String expireFormat = "SECONDS";

    @Column(name = "expire_time", nullable = false)
    private Integer expireTime;

    @Column(name = "expire_token", length = 80, nullable = false, unique = true)
    private String expireToken;

    @Column(name = "is_verify")
    private Boolean isVerify;

    @Column(name = "otp_code", length = 6)
    private String otpCode;

    @Column(name = "register_token", length = 80)
    private String registerToken;

    @Column(name = "chg_dt")
    private LocalDateTime chgDt;

    @Column(name = "user_type", length = 8)
    private String userType;

    @Column(name = "verify_token", length = 80, nullable = false, unique = true)
    private String verifyToken;

    @Column(name = "email", length = 255, nullable = false)
    private String email;
}
