package edu.geekhub.homework.domain;

import java.util.Objects;

public class Product {
    private final int id;
    private String name;
    private double price;

    public Product(String name, double price) {
        this(-1, name, price);
    }

    public Product(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    protected void setName(String name) {
        this.name = name;
    }

    protected void setPrice(Double price) {
        this.price = price;
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

    @Override
    public String toString() {
        return "Product{"
            + "id=" + id
            + ", name='" + name + '\''
            + ", price=" + price
            + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product product)) {
            return false;
        }
        return id == product.id
            && price == product.price
            && name.equals(product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price);
    }
}
