package com.nt.service.category;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.nt.exception.AlreadyExistsException;
import com.nt.exception.ResourceNotFoundException;
import com.nt.model.Category;
import com.nt.repo.CategoryRepository;
import com.nt.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {

	private final CategoryRepository categoryRepository;
	
	
	@Override
	public Category getCategoryById(Long id) {
		
		return categoryRepository
				.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Category not found!!"));
								
	}

	@Override
	public Category getCategoryByName(String name) {
		
		return categoryRepository.findByName(name);
	}

	@Override
	public List<Category> getAllCategory() {
		
		return categoryRepository.findAll();
	}

	@Override
	public Category addCategory(Category category) {
		
		 return  Optional.of(category).filter(c -> !categoryRepository.existsByName(c.getName()))
	                .map(categoryRepository :: save)
	                .orElseThrow(() -> new AlreadyExistsException(category.getName()+" already exists"));
	}

	@Override
	public Category updateCategory(Category category, Long id) {
		// TODO Auto-generated method stub
		return Optional.ofNullable(getCategoryById(id))
				.map( oldCategory -> {
					oldCategory.setName(category.getName());
					
					return categoryRepository.save(oldCategory);
				}).orElseThrow(() -> new ResourceNotFoundException("Category not found!!"));
	}

	@Override
	public void deleteCategory(Long id) {
		categoryRepository
					.findById(id)
					.ifPresentOrElse(
							categoryRepository :: delete,
							() -> {
								throw new ResourceNotFoundException("Category not found");
							});
	}

}
