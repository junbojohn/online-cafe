package com.example.online.cafe.domain.order.entity;

import com.example.online.cafe.domain.menu.entity.Menu;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

//MySQL 테이블과 매핑해주는 역할을 담당하는 Entity 클래스

@Entity
@Getter
//@Setter
@NoArgsConstructor
@Table(name = "order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_Id")
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "address")
    private String address;

    @Column(name = "shipped")
    private Boolean shipped;

    @OneToMany
    @Column(name = "menu_Id")
    private List<Menu> menus = new ArrayList<>();

    @Builder
    public Order(String email, String address, Boolean shipped) {
        this.email = email;
        this.address = address;
        this.shipped = shipped;
    }
}
