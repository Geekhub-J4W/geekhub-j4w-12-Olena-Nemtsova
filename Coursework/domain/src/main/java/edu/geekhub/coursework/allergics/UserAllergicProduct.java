package edu.geekhub.coursework.allergics;

public class UserAllergicProduct {
    private int productId;
    private int userId;

    public UserAllergicProduct(int productId, int userId) {
        this.productId = productId;
        this.userId = userId;
    }

    public UserAllergicProduct() {
        this(-1, -1);
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "UserAllergicProduct{"
               + "productId=" + productId
               + ", userId=" + userId
               + '}';
    }
}
