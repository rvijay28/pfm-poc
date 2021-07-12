package com.hsvj.pfm.repository;

import com.hsvj.pfm.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    public User findByLogin(String login);
    public User findByLoginAndPassword(String login, String password);
}
