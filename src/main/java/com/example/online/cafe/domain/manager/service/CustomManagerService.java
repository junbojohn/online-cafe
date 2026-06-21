package com.example.online.cafe.domain.manager.service;

import com.example.online.cafe.domain.manager.entity.Manager;
import com.example.online.cafe.domain.manager.repository.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
public class CustomManagerService implements UserDetailsService {

    @Autowired
    private ManagerRepository managerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("username inside loadUserByUsername: " + username + "\n");

        Manager manager = managerRepository.findByUsername(username);

        System.out.println("DB password: " + manager.getPassword() + "\n");

        if (manager == null) {
            throw new UsernameNotFoundException("Manager Not Found with given username(해당 닉네임을 가진 관리자가 없습니다): " + username);
        }

        return new org.springframework.security.core.userdetails.User(
                manager.getUsername(),
                manager.getPassword(),
                Collections.emptyList()
        );

        /*
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User Not Found with username: " + username);
        }
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.emptyList()
        );
        */
    }
}
