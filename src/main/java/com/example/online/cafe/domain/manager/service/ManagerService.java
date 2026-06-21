package com.example.online.cafe.domain.manager.service;

import com.example.online.cafe.domain.manager.dto.ManagerDto;
import com.example.online.cafe.domain.manager.entity.Manager;
import com.example.online.cafe.domain.manager.repository.ManagerRepository;
import com.example.online.cafe.domain.menu.dto.MenuManagerDto;
import com.example.online.cafe.domain.menu.entity.Menu;
import com.example.online.cafe.domain.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;

/*
관리자(Manager)가 이용할 수 있는 기능들을 모은 곳

* 신규 상품 등록
* 기존 상품 목록 조회(각 상품 별 판매량, 매출, 별점, 리뷰 정보 포함)
* 기존 상품 수정
* 기존 상품 삭제

*/

@Service
@RequiredArgsConstructor
public class ManagerService {
    private final ManagerRepository managerRepository;
    private final MenuRepository menuRepository;

    // 새로운 관리자 데이터를 생성 및 DB 테이블에 삽입한다.
    public Boolean createManager(String username, String password) {

        // 'Manager' 테이블에 있는 모든 데이터를 가져온다.
        List<Manager> existingManagers = managerRepository.findAll();

        // 가입하려는 관리자의 username이 이미 존재하는지 확인한다.
        // 만약 존재한다면 false를 반환하여 가입을 막는다.
        for (Manager comparingData : existingManagers) {
            if (comparingData.getUsername() == username) {
                return false;
            }
        }

        // 'Manager' 클래스의 builder()를 이용하여 삽입할 정보들을 삽입하고 데이터를 생성한다.
        Manager newManager = Manager.builder()
                .username(username)
                .password(password)
                .build();

        // 생성한 데이터를 DB의 'Manager' 테이블에 삽입한다.
        managerRepository.save(newManager);

        // true 값을 반환하여 새로운 관리자 데이터가 성공적으로 삽입되었음을 알린다.
        return true;
    }

    // 관리자에게 상품 목록에서 보여줄 요소들을 가져온다.
    public MenuManagerDto<Menu> showManagerMenu(int page, Integer price) {
        Pageable pageable =
                PageRequest.of(
                        page,
                        50,
                        Sort.by("id").ascending()
                );

        Page<Menu> managerMenu;

        if (price != null) {
            //customerMenu = menuRepository.showMenuByPriceLowerThan(price, pageable);
            managerMenu = menuRepository.findByPriceLessThan(price, pageable);
        }

        else {
            managerMenu = menuRepository.findAll(pageable);
        }

        // Jpa Repository의 findAll() 특성상 모든 걸 포함하여 보낸다.
        // 다만 Controller에서 보여줄 것들만 분리시켜 보여준다.
        // 나중에 findAll()의 대해 더 자세히 파악하면 여기서부터 보여줄 요소들만 분리시켜 보내게 해야 한다.
        return new MenuManagerDto<>(
                managerMenu.getContent()
        );
    }

    // 관리자에게 새로운 상품 데이터를 생성할 수 있게 해준다.
    public void createMenu() {

    }

    // 관리자에게 기존에 있던 상품의 데이터를 수정할 수 있게 해준다.
    public void editMenu() {

    }

    // 관리자에게 기존에 있던 상품의 데이터를 삭제하게 해준다.
    public void deleteMenu() {

    }

}
