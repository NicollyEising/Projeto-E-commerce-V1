package com.example.demo.dao;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.demo.bean.Product;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {

    // Remova o método findByBrand que está incorreto
    // List<Product> findByBrand(List<String> marcas);

    List<Product> findByPriceBetweenAndBrandIn(
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("brands") List<String> brands);

    List<Product> findByPriceGreaterThanEqualAndBrandIn( double maxPrice,
            List<String> brands);

    List<Product> findByPriceLessThanEqualAndBrandIn(
            @Param("minPrice") Double minPrice,
            @Param("brands") List<String> brands);

    List<Product> findByBrandIn(List<String> brands);

    List<Product> findByPriceBetween(
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice);

  
    List<Product> findByPriceGreaterThanEqual( Double maxPrice);

 
    List<Product> findByPriceLessThanEqual(Double minPrice);
    

    List<Product> findByGenre(String genre);

    List<Product> findByPriceBetweenAndBrandInAndGenre(
            Double minPrice, Double maxPrice, List<String> brands, String genre);

    List<Product> findByPriceGreaterThanEqualAndBrandInAndGenre(
            Double minPrice, List<String> brands, String genre);

    List<Product> findByPriceBetweenAndGenre(
            Double minPrice, Double maxPrice, String genre);

    List<Product> findByPriceGreaterThanEqualAndGenre(
            Double maxPrice, String genre);

    List<Product> findByPriceLessThanEqualAndGenre(
            Double minPrice, String genre);

    List<Product> findByBrandInAndGenre(
            List<String> brands, String genre);

	List<Product> findByBrand(String brands);

    @Query("SELECT p FROM Product p WHERE p.Name LIKE %:name%")
    List<Product> findByNameContaining(String name);

	List<Product> findByPriceLessThanEqualAndBrandInAndGenre(Double minPrice, List<String> brands, String genre);

	


}
