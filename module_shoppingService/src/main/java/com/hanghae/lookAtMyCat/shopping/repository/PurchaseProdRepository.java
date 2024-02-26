package com.hanghae.lookAtMyCat.shopping.repository;

import com.hanghae.lookAtMyCat.shopping.entity.PurchaseProd;
import com.hanghae.lookAtMyCat.shopping.repository.customRepository.CustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseProdRepository extends JpaRepository<PurchaseProd, Long>, CustomRepository {

}
