package com.harusari.chainware.member.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "member")
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "authority_id")
    private Integer authorityId;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "name")
    private String name;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "position")
    private String position;

    @Column(name = "join_at")
    private LocalDateTime joinAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @Builder
    public Member(
            Integer authorityId, String email, String password,
            String name, String phoneNumber, LocalDate birthDate,
            String position, LocalDateTime modifiedAt, boolean isDeleted
    ) {
        this.authorityId = authorityId;
        this.email = email;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.position = position;
        this.joinAt = LocalDateTime.now().withNano(0);
        this.modifiedAt = modifiedAt;
        this.isDeleted = isDeleted;
    }

    public void updateEncodedPassword(String encodedPassword) {
        this.password = encodedPassword;
    }

    public void updateAuthorityId(Integer authorityId) {
        this.authorityId = authorityId;
    }

}