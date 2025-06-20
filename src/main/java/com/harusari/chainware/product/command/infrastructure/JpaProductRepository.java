package com.harusari.chainware.product.command.infrastructure;

import com.harusari.chainware.product.command.domain.aggregate.Product;
import com.harusari.chainware.product.command.domain.repository.ProductRepository;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JpaProductRepository extends ProductRepository, JpaRepository<Product, Long> {
    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(p.productCode, LENGTH(:prefix) + 1) AS int)), 0) " +
            "FROM Product p WHERE p.productCode LIKE CONCAT(:prefix, '%')")
    Integer findMaxNumberByCategoryCode(@Param("prefix") String prefix);
}