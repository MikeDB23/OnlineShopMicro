package com.shop.market.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shop.market.dto.product.ProductDto;
import com.shop.market.dto.product.ProductToSaveDto;
import com.shop.market.exceptions.NotAbleToDeleteException;
import com.shop.market.exceptions.NotFoundException;
import com.shop.market.service.product.ProductService;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;
    
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping()
    public ResponseEntity<List<ProductDto>> getProducts(){
        List<ProductDto> productsDto = productService.getAllProducts();
        return ResponseEntity.ok().body(productsDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable("id") Long idProduct){
        try{
            ProductDto productDto = productService.findProductById(idProduct);
            return ResponseEntity.ok().body(productDto);
        }catch (NotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductDto>> getProductBySearchParam(@RequestParam String searchTerm){
        try{
            List<ProductDto> productsDto = productService.findByNameLike(searchTerm);
            return ResponseEntity.ok().body(productsDto);
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/instock")
    public ResponseEntity<List<ProductDto>> getProductInStock(){
        try{
            List<ProductDto> productsDto = productService.findByStockGreaterThanZero();
            return ResponseEntity.ok().body(productsDto);
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping()
    public ResponseEntity<ProductDto> createNewProduct(@RequestBody ProductToSaveDto productToSaveDto){
        try{
            ProductDto productDto = productService.saveProduct(productToSaveDto);
            System.out.println(productDto);
            return ResponseEntity.ok().body(productDto);
        }catch (Exception e){
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable("id") Long idProduct, 
                                                    @RequestBody ProductToSaveDto productToSaveDto
                                                    ){
        try{
            ProductDto productDto = productService.updateProduct(idProduct,productToSaveDto);
            return ResponseEntity.ok().body(productDto);
        }catch (NotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") Long idProduct){
        try{
            productService.deleteProduct(idProduct);
            return ResponseEntity.ok().build();
        }catch (NotAbleToDeleteException e){
            return ResponseEntity.notFound().build();
        }
    }
}

