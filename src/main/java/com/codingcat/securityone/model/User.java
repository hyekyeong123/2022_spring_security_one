package com.codingcat.securityone.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// ORM - Object Relation Mapping

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Builder
    public User(String username, String password, String email, String role, String provider, String providerId, Timestamp createDate) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
        this.createDate = createDate;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String username;
    private String password;
    private String email;
    private String role; //ROLE_USER, ROLE_ADMIN, ROLE_MANAGER

    // OAuth를 위해 구성한 추가 필드 2개
    private String provider; // google
    private String providerId; // google - sub

    // @CreationTimeStamp는 INSERT 쿼리가 발생할 때, 현재 시간을 자도으로 값으로 채워서 쿼리를 생성한다.
    @CreationTimestamp
    private Timestamp createDate;
}