package com.project.stationery_be_server.repository;

import com.project.stationery_be_server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,String> {
}
