package com.shop.market.service.product;

import java.util.List;

import org.springframework.stereotype.Service;

import com.shop.market.dto.product.ProductDto;
import com.shop.market.dto.product.ProductMapper;
import com.shop.market.dto.product.ProductToSaveDto;
import com.shop.market.entities.Product;
import com.shop.market.exceptions.NotAbleToDeleteException;
import com.shop.market.exceptions.NotFoundException;
import com.shop.market.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService{
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;    

    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                            .orElseThrow(()-> new NotAbleToDeleteException("Product not found"));
        
        productRepository.delete(product);
    }


    @Override
    public ProductDto findProductById(Long id) {
        Product product = productRepository.findById(id)
                            .orElseThrow(() -> new NotFoundException("Product not found"));
        return productMapper.entityToDto(product);
    }

    @Override
    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                    .map(product -> productMapper.entityToDto(product))
                    .toList();
    }

    @Override
    public ProductDto saveProduct(ProductToSaveDto productToSaveDto) {
        Product product = productMapper.saveDtoToEntity(productToSaveDto);
        Product savedProduct = productRepository.save(product);
        return productMapper.entityToDto(savedProduct);
    }

    @Override
    public ProductDto updateProduct(Long id, ProductToSaveDto product) {
        return productRepository.findById(id)
                .map(productDB -> {
                    productDB.setName(product.name());
                    productDB.setPrice(product.price());
                    productDB.setStock(product.stock());

                    Product savedProduct = productRepository.save(productDB);
                    return productMapper.entityToDto(savedProduct);
                }).orElseThrow(() -> new NotFoundException("Product not found"));
    }
    
    @Override
    public List<ProductDto> findByNameLike(String searchTerm) {
        List<Product> products = productRepository.findByNameLike(searchTerm);
        return products.stream()
                .map(product -> productMapper.entityToDto(product))
                .toList();
    }

    @Override
    public List<ProductDto> findByPriceLessThanAndStockLessThan(Double price, Integer stock) {
        List<Product> products = productRepository.findByPriceLessThanAndStockLessThan(price, stock);
        return products.stream()
                .map(product -> productMapper.entityToDto(product))
                .toList();
    }

    @Override
    public List<ProductDto> findByStockGreaterThanZero() {
        List<Product> products = productRepository.findByStockGreaterThanZero();
        return products.stream()
                .map(product -> productMapper.entityToDto(product))
                .toList();
    }
}
