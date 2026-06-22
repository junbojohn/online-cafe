package com.example.online.cafe.domain.review.entity;

import com.example.online.cafe.domain.menu.entity.Menu;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//MySQL 테이블과 매핑해주는 역할을 담당하는 Entity 클래스

@Entity
@Getter
//@Setter
@NoArgsConstructor
@Table(name = "review")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_Id")
    private Long id;

    @Column(name = "review_details")
    private String review_details;

    @ManyToOne
    //@JoinColumn(name = "menu_Id")
    @JoinColumn(name = "menu")
    private Menu menu;
    //private Long menu_id;

    @Builder
    public Review(String review_details, Menu menu) {
        this.review_details = review_details;
        this.menu = menu;
    }
}
