package com.example.online.cafe.domain.menu.entity;

import com.example.online.cafe.domain.orders.entity.Orders;
import com.example.online.cafe.domain.review.entity.Review;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
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
    private String menuName;

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
    @OneToMany(mappedBy = "menu_number")
    private List<Orders> orders = new ArrayList<>();
    */

    /*
    @ManyToOne
    @JoinColumn(name = "orders")
    private Orders orders;
    */

    @Builder
    public Menu(String menuName, Integer price, double rating, Long sales, Long sales_volume, List<Review> reviews) {
        this.menuName = menuName;
        this.price = price;
        this.rating = rating;
        this.sales = sales;
        this.sales_volume = sales_volume;
        this.reviews = reviews;
    }

    public void updateMenuName(String menuName) {
        this.menuName = menuName;
    }

    public void updateMenuPrice(Integer price) {
        this.price = price;
    }

    public void addReview(Review newReview) {
        this.reviews.add(newReview);
    }
}
