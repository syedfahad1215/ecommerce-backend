package com.nt.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nt.exception.AlreadyExistsException;
import com.nt.exception.ResourceNotFoundException;
import com.nt.model.Category;
import com.nt.response.ApiResponse;
import com.nt.service.category.ICategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/categories")
public class CategoryController {
	
	private final ICategoryService categoryService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllCategories() 
    {
        try {
            List<Category> categories = categoryService.getAllCategory();
            return  ResponseEntity.ok(new ApiResponse("Found!", categories));
            
        } catch (Exception e) {
           return ResponseEntity
        		   .status(HttpStatus.INTERNAL_SERVER_ERROR)
        		   .body(new ApiResponse("Error:", HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addCategory(@RequestBody Category name) 
    {
        try {
            Category theCategory = categoryService.addCategory(name);
            return  ResponseEntity.ok(new ApiResponse("Success", theCategory));
        } catch (AlreadyExistsException e) {
           return ResponseEntity
        		   .status(HttpStatus.CONFLICT)
        		   .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/category/{id}/category")
    public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long id){
        try {
            Category theCategory = categoryService.getCategoryById(id);
            return  ResponseEntity.ok(new ApiResponse("Found", theCategory));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity
            		.status(HttpStatus.NOT_FOUND)
            		.body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/category/{name}")
    public ResponseEntity<ApiResponse> getCategoryByName(@PathVariable String name){
        try {
            Category theCategory = categoryService.getCategoryByName(name);
            return  ResponseEntity.ok(new ApiResponse("Found", theCategory));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity
            		.status(HttpStatus.NOT_FOUND)
            		.body(new ApiResponse(e.getMessage(), null));
        }
    }


    @DeleteMapping("/category/{id}/delete")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Long id){
        try {
            categoryService.deleteCategory(id);
            return  ResponseEntity.ok(new ApiResponse("Found", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity
            		.status(HttpStatus.NOT_FOUND)
            		.body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/category/{id}/update")
    public ResponseEntity<ApiResponse> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        try {
            Category updatedCategory = categoryService.updateCategory(category, id);
            return ResponseEntity.ok(new ApiResponse("Update success!", updatedCategory));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

}
