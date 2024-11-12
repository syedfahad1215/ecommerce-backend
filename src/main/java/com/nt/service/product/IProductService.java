package com.nt.service.product;

import java.util.List;

import com.nt.dto.ProductDto;
import com.nt.model.Product;
import com.nt.request.AddProductRequest;
import com.nt.request.ProductUpdateRequest;

public interface IProductService {
	
	Product addProduct(AddProductRequest request);
	
	Product getProductById(long id);
	
	void deleteProductById(long id);
	
	Product updateProduct(ProductUpdateRequest request, Long productId);
	
	List<Product> getAllProducts();
	
	List<Product> getProductsByCategory(String category);
	
	List<Product> getProductsByBrand(String brand);
	
	List<Product> getProductsByCategoryAndBrand(String category, String brand);
	
	List<Product> getProductsByName(String name);
	
	List<Product> getProductsByBrandAndName(String brand, String name);
	
	Long countProductsByBrandAndName(String Brand, String name);
	
	List<ProductDto> getConvertedProducts(List<Product> products);

	ProductDto convertToDto(Product product);

}
