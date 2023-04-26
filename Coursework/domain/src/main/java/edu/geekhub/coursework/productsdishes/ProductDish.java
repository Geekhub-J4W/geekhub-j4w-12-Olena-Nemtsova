package edu.geekhub.coursework.productsdishes;

import java.util.Objects;

public class ProductDish {
    private int productId;
    private int dishId;
    private int productQuantity;

    public ProductDish(int productId, int dishId, int productQuantity) {
        this.productId = productId;
        this.dishId = dishId;
        this.productQuantity = productQuantity;
    }

    public ProductDish() {
        this(-1, -1, 0);
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getDishId() {
        return dishId;
    }

    public void setDishId(int dishId) {
        this.dishId = dishId;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    @Override
    public String toString() {
        return "ProductDish{"
               + "productId=" + productId
               + ", dishId=" + dishId
               + ", productQuantity=" + productQuantity
               + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProductDish that = (ProductDish) o;

        return productId == that.productId
               && dishId == that.dishId
               && productQuantity == that.productQuantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, dishId, productQuantity);
    }
}
