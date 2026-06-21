package com.example.online.cafe.domain.manager.entity;

import jakarta.persistence.*;
import lombok.*;

//MySQL 테이블과 매핑해주는 역할을 담당하는 Entity 클래스

@Entity
@Getter
//@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "managers")
public class Manager {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "manager_Id")
    private Long id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @Builder
    public Manager(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
