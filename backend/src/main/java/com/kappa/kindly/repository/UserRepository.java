package com.kappa.kindly.repository;

import com.kappa.kindly.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	User findById(long id);
	
}
