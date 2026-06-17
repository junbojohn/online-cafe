package com.example.online.cafe.domain.orders.entity;

import com.example.online.cafe.domain.menu.entity.Menu;
import com.example.online.cafe.domain.review.entity.Review;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

//MySQL 테이블과 매핑해주는 역할을 담당하는 Entity 클래스

@Entity
@Getter
//@Setter
@NoArgsConstructor
@Table(name = "orders")
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_Id")
    private Long id;

    @Column(name = "order_number")
    private Long order_number;

    //Must connect this with 'Menu' table using foreign key thing
    //@Column(name = "menu")
    //private Long menu;

    @Column(name = "menu_quantity")
    private Long menu_quantity;

    @Column(name = "email")
    private String email;

    @Column(name = "address")
    private String address;

    @Column(name = "shipped")
    private Boolean shipped;

    /*
    @OneToMany(mappedBy = "orders")
    private List<Menu> menus = new ArrayList<>();
    */

    /*
    @OneToMany
    @Column(name = "menu_Id")
    private List<Menu> menus = new ArrayList<>();
    */

    @Builder
    public Orders(String email, String address, Boolean shipped) {
        this.email = email;
        this.address = address;
        this.shipped = shipped;
    }
}
