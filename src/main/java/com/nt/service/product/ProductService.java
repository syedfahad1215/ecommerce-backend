package com.nt.service.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
//import org.modelmapper.ModelMapper;

import com.nt.dto.ImageDto;
import com.nt.dto.ProductDto;
import com.nt.exception.ProductNotFoundException;
import com.nt.model.Category;
import com.nt.model.Image;
import com.nt.model.Product;
import com.nt.repo.CategoryRepository;
import com.nt.repo.ImageRepository;
import com.nt.repo.ProductRepository;
import com.nt.request.AddProductRequest;
import com.nt.request.ProductUpdateRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService{

	private final ProductRepository productRepository;
	
	private final CategoryRepository categoryRepository;
	

    private final ImageRepository imageRepository;
	
	
	
	@Override
	public Product addProduct(AddProductRequest request) {
		/*
		 	Check if the category is found in the DB
		 	If yes, set it as the new product category
		 	Tf not then save the category as new category
		 	Then set it as a new product category
		 */
		Category category = Optional.ofNullable(
				categoryRepository
					.findByName(request.getCategory().getName())
				).orElseGet( () -> {
					Category newCategory = new Category(request.getCategory().getName());
					return categoryRepository.save(newCategory);
				});
		
		request.setCategory(category);
		
		
		return productRepository
				.save(createProduct(request, category));
	}
	
	
	private Product createProduct(AddProductRequest request, Category category) {
		
		return new Product(
				request.getName(),
				request.getBrand(),
				request.getPrice(),
				request.getInventory(),
				request.getDescription(),
				request.getCategory()
				);
	}
	

	@Override
	public Product getProductById(long id) {
		
		return productRepository
				.findById(id)
				.orElseThrow(() -> new ProductNotFoundException("Product not found"));
				
	}

	@Override
	public void deleteProductById(long id) {
		
		productRepository
		.findById(id)
		.ifPresentOrElse(productRepository::delete, 
				() -> {
					throw new ProductNotFoundException("Product not found!!");
				});
		
	}

	@Override
	public Product updateProduct(ProductUpdateRequest request, Long productId) {
		return productRepository.findById(productId)
				.map(existingProduct -> updateExistingProduct(existingProduct, request))
				.map(productRepository:: save)
				.orElseThrow(() -> new ProductNotFoundException("Product not found"));
		
	}
	
	private Product updateExistingProduct(
			Product existingProduct, 
			ProductUpdateRequest request) 
	{
		existingProduct.setName(request.getName());
		existingProduct.setBrand(request.getBrand());
		existingProduct.setPrice(request.getPrice());
		existingProduct.setInventory(request.getInventory());
		existingProduct.setDescription(request.getDescription());
		
		Category category = categoryRepository.findByName(
											request.getCategory()
													.getName());
		
		existingProduct.setCategory(category);
		
		return existingProduct;
	}
	
	

	@Override
	public List<Product> getAllProducts() {
		
		return productRepository.findAll();
	}

	@Override
	public List<Product> getProductsByCategory(String category) {
		
		return productRepository
				.findByCategoryName(category);
	}

	@Override
	public List<Product> getProductsByBrand(String brand) {
		
		return productRepository
				.findByBrand(brand);
	}

	@Override
	public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
		
		return productRepository
				.findByCategoryNameAndBrand(category, brand);
	}

	@Override
	public List<Product> getProductsByName(String name) {
		
		return productRepository.findByName(name);
	}

	@Override
	public List<Product> getProductsByBrandAndName(String brand, String name) {
		
		return productRepository.findByBrandAndName(brand, name);
	}

	@Override
	public Long countProductsByBrandAndName(String brand, String name) {
		
		return productRepository.countByBrandAndName(brand, name);
	}
	
	 @Override
	 public List<ProductDto> getConvertedProducts(List<Product> products) {
	      return products.stream().map(this::convertToDto).toList();
	 }


	

	 @Override
	 public ProductDto convertToDto(Product product) {
		 
		 List<Image> images = imageRepository.findByProductId(product.getId());
	        
		 List<ImageDto> imageDtos = new ArrayList<>();
		 for(Image image: images) {
			 ImageDto imageDto = new ImageDto();
			 imageDto.setDownloadUrl(image.getDownloadUrl());
			 imageDto.setImageId(image.getId());
			 imageDto.setImageName(image.getFileName());
			 imageDtos.add(imageDto);
		 }
		 
		 
		 ProductDto productDto = new ProductDto();
		 
		 productDto.setId(product.getId());
		 productDto.setName(product.getName());
		 productDto.setBrand(product.getBrand());
		 productDto.setPrice(product.getPrice());
		 productDto.setCategory(product.getCategory());
		 productDto.setInventory(product.getInventory());
		 productDto.setDescription(product.getDescription());
		 
					
	        productDto.setImages(imageDtos);
	        return productDto;
	  }
	  
	  

}
