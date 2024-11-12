package com.nt.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nt.model.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
	
//	 Cart findByUserId(Long userId);
}
