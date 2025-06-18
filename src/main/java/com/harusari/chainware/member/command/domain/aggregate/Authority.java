package com.harusari.chainware.member.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "authority")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Authority {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "authority_id")
    private Integer authorityId;

    @Enumerated(EnumType.STRING)
    @Column(name = "authority_name")
    private MemberAuthorityType authorityName;

    @Column(name = "authority_label_kr")
    private String authorityLabelKr;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Builder
    public Authority(
            MemberAuthorityType authorityName, String authorityLabelKr, LocalDateTime createdAt,
            LocalDateTime modifiedAt, LocalDateTime deletedAt
    ) {
        this.authorityName = authorityName;
        this.authorityLabelKr = authorityLabelKr;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.deletedAt = deletedAt;
    }

}