package edu.geekhub.homework.domain;

import java.util.Arrays;
import java.util.Objects;

public class Product {
    private int id;
    private String name;
    private double price;
    private int categoryId;
    private byte[] image;
    private int quantity;

    public Product(int id,
                   String name,
                   double price,
                   int categoryId,
                   byte[] image,
                   int quantity) {

        this.id = id;
        this.name = name;
        this.price = price;
        this.categoryId = categoryId;
        this.image = image;
        this.quantity = quantity;
    }

    public Product(String name, double price, int productCategoryId) {
        this(-1, name, price, productCategoryId, null, 0);
    }

    public Product() {
        this(-1, null, 0, -1, null, 0);
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

    public void setImage(byte[] image) {
        this.image = image;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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

    public byte[] getImage() {
        return image;
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
               && quantity == product.quantity
               && Objects.equals(name, product.name)
               && Arrays.equals(image, product.image);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, name, price, categoryId, quantity);
        result = 31 * result + Arrays.hashCode(image);
        return result;
    }

    @Override
    public String toString() {
        return name + " - " + price;
    }
}
