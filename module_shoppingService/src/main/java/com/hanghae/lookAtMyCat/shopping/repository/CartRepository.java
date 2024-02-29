package com.hanghae.lookAtMyCat.shopping.repository;

import com.hanghae.lookAtMyCat.shopping.entity.Cart;
import com.hanghae.lookAtMyCat.shopping.repository.customRepository.CustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long>, CustomRepository {

}
