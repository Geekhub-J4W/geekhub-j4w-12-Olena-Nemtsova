package edu.geekhub.homework.domain;


public record Product(int id, String name, double price, int categoryId, String imagePath) {

    public Product(String name, double price, int productCategoryId) {
        this(-1, name, price, productCategoryId, null);
    }


    @Override
    public String toString() {
        return "Product{"
               + "id=" + id
               + ", name='" + name + '\''
               + ", price=" + price
               + ", categoryId=" + categoryId
               + '}';
    }

    public Product changeId(int id) {
        return new Product(id, name(), price(), categoryId(), imagePath());
    }
}
