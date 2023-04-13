package edu.geekhub.coursework.productsdishes;

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
}
