package edu.geekhub.homework.domain;

import java.util.Objects;

public class Product {
    private int id;
    private String name;
    private double price;
    private int categoryId;
    private String imagePath;

    public Product(int id, String name, double price, int categoryId, String imagePath) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.categoryId = categoryId;
        this.imagePath = imagePath;
    }

    public Product(String name, double price, int productCategoryId) {
        this(-1, name, price, productCategoryId, null);
    }

    public Product() {
        this(-1, null, 0, -1, null);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getImagePath() {
        return imagePath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Product product = (Product) o;
        return id == product.id
               && Double.compare(product.price, price) == 0
               && categoryId == product.categoryId
               && Objects.equals(name, product.name)
               && Objects.equals(imagePath, product.imagePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, categoryId, imagePath);
    }

    @Override
    public String toString() {
        return name + " - " + price;
    }
}
