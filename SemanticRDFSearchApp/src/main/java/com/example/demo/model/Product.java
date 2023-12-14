package com.example.demo.model;

public class Product {
	private String title;
	private float price;
	private String category;
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Product(String title, float price, String category) {
		super();
		this.title = title;
		this.price = price;
		this.category = category;
	}

}
