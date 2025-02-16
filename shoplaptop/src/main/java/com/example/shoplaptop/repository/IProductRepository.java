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
    @Query("select p from Product p " +
            "where (:productName is null or lower(p.name) like lower(concat('%', :productName, '%'))) " +
            "and (:categoryName is null or lower(p.category.name) like lower(concat('%', :categoryName, '%'))) " +
            "and (:priceMin is null or (CASE WHEN p.discount is not null and p.discount > 0 " +
            "      THEN (p.price * (100 - p.discount)) / 100 ELSE p.price END) >= :priceMin) " +
            "and (:priceMax is null or (CASE WHEN p.discount is not null and p.discount > 0 " +
            "      THEN (p.price * (100 - p.discount)) / 100 ELSE p.price END) <= :priceMax)")
    Page<Product> findByFilters(@Param("productName") String productName,
                                @Param("categoryName") String categoryName,
                                @Param("priceMin") BigDecimal priceMin,
                                @Param("priceMax") BigDecimal priceMax,
                                Pageable pageable);


    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId")
    List<Product> findProductsByCategoryId(@Param("categoryId") Long categoryId);

}
