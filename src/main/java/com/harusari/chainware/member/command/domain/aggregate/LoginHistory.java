package com.harusari.chainware.member.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "login_history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginHistory {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "login_history_id")
    private Long loginHistoryId;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "login_at")
    private LocalDateTime loginAt;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "browser")
    private String browser;

    @Builder
    public LoginHistory(Long memberId, String ipAddress, String browser) {
        this.memberId = memberId;
        this.loginAt = LocalDateTime.now().withNano(0);
        this.ipAddress = ipAddress;
        this.browser = browser;
    }

}