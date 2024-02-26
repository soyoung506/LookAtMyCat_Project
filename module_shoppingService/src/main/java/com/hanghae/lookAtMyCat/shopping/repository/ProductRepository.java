package com.hanghae.lookAtMyCat.shopping.repository;

import com.hanghae.lookAtMyCat.shopping.repository.customRepository.CustomRepository;
import com.hanghae.lookAtMyCat.shopping.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long>, CustomRepository {

}
