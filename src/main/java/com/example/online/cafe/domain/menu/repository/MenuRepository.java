package com.example.online.cafe.domain.menu.repository;

import com.example.online.cafe.domain.menu.entity.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/*
DB 작업을 담당하는 인터페이스 클래스로 DAO(Data Access Object)다.
CRUD 작업을 담당한다.

JpaRepository<T, ID>
T: 엔티티(Entity) 클래스. DB 테이블과 매핑되는 자바 객체(Object) 클래스
ID: 해당 엔티티(Entity)의 기본키(Primary Key) 데이터 타입. 엔티티 클래스 @Id 어노테이션(Annotation)이 붙은 필드의 데이터 타입
google search: JpaRepository<T, ID>
*/

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    //Optional<Menu> findMenuByName(String menu_name);
    //Page<Menu> showMenuByAlphabetOrder();
    //Page<Menu> showMenuByPriceLowerThan(Integer price, Pageable pageable);
    Page<Menu> findByPriceLessThan(Integer price, Pageable pageable);
}
