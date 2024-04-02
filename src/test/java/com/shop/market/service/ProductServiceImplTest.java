package com.shop.market.service;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.shop.market.TestUtil;
import com.shop.market.dto.product.ProductDto;
import com.shop.market.dto.product.ProductMapper;
import com.shop.market.dto.product.ProductToSaveDto;
import com.shop.market.entities.Product;
import com.shop.market.repository.ProductRepository;
import com.shop.market.service.product.ProductServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    List<Product> products = TestUtil.genProduct();
    Product product = products.get(0);
    ProductDto productDto = new ProductDto(product.getId(), product.getName(), product.getPrice(), product.getStock());
    List<Product> answerProducts = new ArrayList<>();

    @BeforeEach
    void setUp(){
        product.setId(1l);
        answerProducts.clear();
        answerProducts.add(product);
    }

    @Test
    void testDeleteProduct() {
        // Given a product, when delete, do nothing
        willDoNothing().given(productRepository).delete(any());
        given(productRepository.findById(product.getId())).willReturn(Optional.of(product));
        productService.deleteProduct(1l);

        verify(productRepository, times(1)).delete(any());
    }

    @Test
    void testFindByNameLike() {
        // Given a name (string), when find by name, return product.
        given(productRepository.findByNameLike(product.getName())).willReturn(answerProducts);
        List<ProductDto> retuValue = productService.findByNameLike("mondongo");
        assertThat(retuValue).isNotNull();
    }

    @Test
    void testFindByPriceLessThanAndStockLessThan() {
        // Given a price and a stock, when find lower than price and stock, return porduct.
        given(productRepository.findByPriceLessThanAndStockLessThan(product.getPrice()+1, product.getStock()+1)).willReturn(answerProducts);
        List<ProductDto> retuValue = productService.findByPriceLessThanAndStockLessThan(6901.00, 11);
        assertThat(retuValue).isNotNull();
    }

    @Test
    void testFindByStockGreaterThanZero() {
        // When find by stock, return producto.
        given(productRepository.findByStockGreaterThanZero()).willReturn(answerProducts);
        List<ProductDto> retuValue = productService.findByStockGreaterThanZero();
        assertThat(retuValue).isNotNull();
    }

    @Test
    void testFindProductById() {
        // Given an Id, when find by Id, return product.
        given(productRepository.findById(product.getId())).willReturn(Optional.of(product));
        given(productMapper.entityToDto(any())).willReturn(productDto);
        ProductDto retuValue = productService.findProductById(1l);
        assertThat(retuValue).isNotNull();
    }

    @Test
    void testGetAllProducts() {
        // When get all products, return all products.
        given(productRepository.findAll()).willReturn(products);
        List<ProductDto> retuValue = productService.getAllProducts();
        assertThat(retuValue).isNotNull();
        assertThat(retuValue).asList().hasSize(5);
    }

    @Test
    void testSaveProduct() {
        // Given a product, when save, return product.
        given(productMapper.saveDtoToEntity(any())).willReturn(product);
        given(productMapper.entityToDto(any())).willReturn(productDto);
        given(productRepository.save(any())).willReturn(product);
        ProductToSaveDto productToSave = new ProductToSaveDto(product.getName(), product.getPrice(), product.getStock());
        ProductDto retuValue = productService.saveProduct(productToSave);
        assertThat(retuValue).isNotNull();
    }

    @Test
    void testUpdateProduct() {
        // Given a product, when update, return product.
        ProductDto productDtoToUpdate = new ProductDto(product.getId(), product.getName(), product.getPrice(), 2);
        Product productUpdate = product;
        productUpdate.setStock(2);
        given(productMapper.entityToDto(any())).willReturn(productDtoToUpdate);
        given(productRepository.findById(any())).willReturn(Optional.of(product));
        given(productRepository.save(any())).willReturn(productUpdate);
        ProductToSaveDto productToSave = new ProductToSaveDto(product.getName(), product.getPrice(), 2);
        ProductDto retuValue = productService.updateProduct(1l, productToSave);
        assertThat(retuValue).isNotNull();
        assertThat(retuValue.id()).isEqualTo(1l);
        assertThat(retuValue.stock()).isEqualTo(2);
    }
}
