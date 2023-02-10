package edu.geekhub.homework.domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ProductRepository {
    private final List<Product> products;
    private final ProductValidator productValidator;
    private int counter = 0;

    protected ProductRepository() {
        products = new ArrayList<>();
        productValidator = new ProductValidator();
    }

    protected List<Product> getProducts() {
        return products;
    }

    protected void addProduct(Product product) {
        productValidator.validate(product);

        if (product.getId() == -1) {
            product = new Product(++counter, product.getName(), product.getPrice());
            products.add(product);
        } else {
            products.add(product);
            counter = products.stream()
                .map(Product::getId)
                .max(Comparator.comparingInt(Integer::intValue))
                .orElse(counter);
        }
    }

    protected Product getProductById(int id) {
        return products.stream()
            .filter(product -> product.getId() == id)
            .findFirst()
            .orElse(null);
    }

    protected boolean deleteProductById(int id) {
        Product productToDel = getProductById(id);
        if (productToDel != null) {
            products.remove(productToDel);
            return true;
        }
        return false;
    }

    protected boolean updateProductById(Product product, int id) {
        Product productToEdit = getProductById(id);
        if (productToEdit != null) {
            productValidator.validate(product);

            int index = products.indexOf(productToEdit);
            products.get(index).setName(product.getName());
            products.get(index).setPrice(product.getPrice());

            return true;
        }
        return false;
    }
}
