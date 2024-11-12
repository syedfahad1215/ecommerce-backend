package com.nt.request;

import java.math.BigDecimal;

import com.nt.model.Category;

import lombok.Data;

@Data
public class AddProductRequest {
	
	private Long id;
    private String name;
    private String brand;
    private BigDecimal price;
    private int inventory;
    private String description;
    private Category category;

}
