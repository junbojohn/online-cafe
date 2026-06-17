package com.example.online.cafe.domain.menu.entity;

import com.example.online.cafe.domain.orders.entity.Orders;
import com.example.online.cafe.domain.review.entity.Review;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

//MySQL 테이블과 매핑해주는 역할을 담당하는 Entity 클래스

@Entity
@Getter
//@Setter
@NoArgsConstructor
@Table(name = "menu")
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_Id")
    private Long id;

    @Column(name = "menu_name")
    private String menu_name;

    @Column(name = "price")
    private Integer price;

    @Column(name = "rating")
    private double rating;

    @Column(name = "sales")
    private Long sales;

    @Column(name = "sales_volume")
    private Long sales_volume;

    @OneToMany(mappedBy = "menu")
    private List<Review> reviews = new ArrayList<>();

    /*
    @ManyToOne
    @JoinColumn(name = "orders")
    private Orders orders;
    */

    @Builder
    public Menu(String menu_name, Integer price, double rating, Long sales, Long sales_volume) {
        this.menu_name = menu_name;
        this.price = price;
        this.rating = rating;
        this.sales = sales;
        this.sales_volume = sales_volume;
    }
}
