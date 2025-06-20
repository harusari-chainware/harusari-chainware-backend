package com.harusari.chainware.product.command.domain.repository;

import com.harusari.chainware.product.command.domain.aggregate.Product;

import java.util.Optional;

public interface ProductRepository {

    Product save(Product product);

    Optional<Product> findById(Long productId);

    boolean existsByCategoryId(Long categoryId);

    Integer findMaxNumberByCategoryCode(String prefix);

    boolean existsByProductName(String productName);

}
