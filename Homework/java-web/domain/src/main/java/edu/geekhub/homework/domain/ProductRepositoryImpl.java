package edu.geekhub.homework.domain;

import java.util.Comparator;
import java.util.List;

public class ProductRepositoryImpl implements ProductRepository {
    private final List<Product> products;
    private final ProductValidator productValidator;
    private int counter = 0;

    public ProductRepositoryImpl(List<Product> products, ProductValidator productValidator) {
        this.products = products;
        this.productValidator = productValidator;
    }

    @Override
    public List<Product> getProducts() {
        return products;
    }

    @Override
    public void addProduct(Product product) {
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

    @Override
    public Product getProductById(int id) {
        return products.stream()
            .filter(product -> product.getId() == id)
            .findFirst()
            .orElse(null);
    }

    @Override
    public boolean deleteProductById(int id) {
        Product productToDel = getProductById(id);
        if (productToDel != null) {
            products.remove(productToDel);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateProductById(Product product, int id) {
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
