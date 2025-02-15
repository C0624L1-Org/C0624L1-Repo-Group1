package com.example.shoplaptop.repository;

import com.example.shoplaptop.model.Category;
import com.example.shoplaptop.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface IProductRepository extends JpaRepository<Product, Integer> {
    Page<Product> findAll(Pageable pageable);

    List<Product> findAll();

    Product getById(Integer id);

    Product save(Product product);

    boolean existsByName(String name);

    void delete(Product product);

    long count();

    @Query(value = "select p from Product p where (:name is null or lower(p.name) like lower(concat('%',:name,'%')))" +
            "and (:brand is null or lower(p.category.name) like lower(concat('%',:brand,'%')))")
    Page<Product> findByNameContainingAndCategory(@Param("name") String name, @Param("brand") String brand, Pageable pageable);

    //Filter
    @Query("SELECT p FROM Product p " +
            "WHERE (:brandId IS NULL OR p.category.id = :brandId) " +
            "AND (:priceMin IS NULL OR p.price >= :priceMin) " +
            "AND (:priceMax IS NULL OR p.price <= :priceMax)")
    Page<Product> searchProducts(@Param("brandId") Long brandId,
                                 @Param("priceMin") BigDecimal priceMin,
                                 @Param("priceMax") BigDecimal priceMax,
                                 Pageable pageable);


}
