package com.ergasia3.cinemaclasses;

public class Film {
	private int Id;
	private String Title, Category, Description;

	//constructor
	public Film(int filmId, String filmTitle, String filmCategory, String filmDescription) {
		this.Id = filmId;
		this.Title = filmTitle;
		this.Category = filmCategory;
		this.Description = filmDescription;
	}

	//getters/setters
	public int getId() { return Id; }

	public void setId(int id) { this.Id = id; }

	public String getTitle() { return Title; }

	public void setTitle(String title) { this.Title = title; }

	public String getCategory() { return Category; }

	public void setCategory(String category) { this.Category = category; }

	public String getDescription() { return Description; }

	public void setDescription(String description) { this.Description = description; }

	@Override
	public String toString()
	{
		return String.format("Film{ ID: %s, Title: %s, Category: %s, Description: %s }",
				Id, Title, Category, Description);
	}
}