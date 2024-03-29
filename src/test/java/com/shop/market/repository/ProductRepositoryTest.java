package com.shop.market.repository;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.shop.market.AbstractsIntegrationDBTest;
import com.shop.market.TestUtil;
import com.shop.market.entities.Product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProductRepositoryTest extends AbstractsIntegrationDBTest{
    ProductRepository productRepository;

    @Autowired
    public ProductRepositoryTest(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    void initProductTest() {
        List<Product> products = TestUtil.genProduct(); 
        productRepository.saveAll(products);
        productRepository.flush();
    }

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
    }

    
    //create Test
    @Test
    void givenAProduct_whenSave_thenProductwithId(){
        Product product = Product.builder()
                        .name("Something legal")
                        .price(999999.00)
                        .stock(1)
                        .build();
        Product productSaved = productRepository.save(product);
        assertThat(productSaved.getId()).isNotNull();
        assertThat(productRepository.findAll()).hasSize(1);
    }

    //read test
    @Test
    void whenFindAll_thenGetAllProducts(){
        initProductTest();
        List<Product> products = productRepository.findAll();
        assertThat(products).isNotEmpty();
        assertThat(products).hasSize(5);
    }

    //Update test
    @Test
    void givenAProductAndName_whenSetName_thenUpdateProductsName(){
        initProductTest();
        List<Product> products = productRepository.findAll();
        assertThat(products).isNotEmpty();
        
        Product product = products.get(1);
        Product product_copy = products.get(1);
        product.setName("cooler ibuprofeno");
        productRepository.save(product);
        
        Optional<Product> updatedProductOptional = productRepository.findById(product_copy.getId());
        assertTrue(updatedProductOptional.isPresent(), "Product not found");

        Product updatedProduct = updatedProductOptional.get();
        assertEquals(product_copy.getName(), updatedProduct.getName(), "Name not updated");
    }

    //Delete test
    @Test
    void givenAProduct_whenDelete_thenDeleteProduct(){
        initProductTest();
        Product product = productRepository.findAll().get(3);
        Long size = productRepository.count();
        productRepository.delete(product);
        assertEquals(size-1,productRepository.count());
    }

    @Test
    void givenAProduct_whenFindByName_thenGetProduct() {
            initProductTest();
            List<Product> products = productRepository.findByNameLike("mondongo");
            assertThat(products).isNotEmpty(); 
    }

    @Test
    void whenFindByStockGreaterThanZero_thenGetProductsWithStock() {
        initProductTest();
        List<Product> products = productRepository.findByStockGreaterThanZero();
        assertThat(products).hasSize(3);
    }

    @Test
    void givenAPriceAndStock_whenFindByPriceLessThanAndStockLessThan_thenGetProductsWithPriceAndStockLowerThanProvided() {
        initProductTest();
        List<Product> products = productRepository.findByPriceLessThanAndStockLessThan(3000.00, 2);
        assertThat(products).hasSize(1);
    }
}