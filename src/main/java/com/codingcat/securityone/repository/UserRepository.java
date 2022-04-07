package com.codingcat.securityone.repository;

import com.codingcat.securityone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

// CRUD 함수를 JpaRepository가 들고 있음
// @Repository라는 어노테이션이 없어도 IOC 가능
public interface UserRepository extends JpaRepository<User, Integer> {

    public User findByUsername(String username);


}
