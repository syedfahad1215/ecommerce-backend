package com.nt.service.category;

import java.util.List;

import com.nt.model.Category;

public interface ICategoryService {
	
	Category getCategoryById(Long id);
	
	Category getCategoryByName(String name);
	
	List<Category> getAllCategory();
	
	Category addCategory(Category category);
	
	Category updateCategory(Category category, Long id);
	
	void deleteCategory(Long id);

}
