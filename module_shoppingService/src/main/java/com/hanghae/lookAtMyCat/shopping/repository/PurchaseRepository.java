package com.hanghae.lookAtMyCat.shopping.repository;

import com.hanghae.lookAtMyCat.shopping.entity.Purchase;
import com.hanghae.lookAtMyCat.shopping.repository.customRepository.CustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<Purchase, Long>, CustomRepository {

}
