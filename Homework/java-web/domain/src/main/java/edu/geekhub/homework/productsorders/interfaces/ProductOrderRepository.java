package edu.geekhub.homework.productsorders.interfaces;

import edu.geekhub.homework.productsorders.ProductOrder;
import java.util.List;

public interface ProductOrderRepository {

    List<ProductOrder> getProductOrders();

    ProductOrder getRelationByProductAndOrderId(int productId, int orderId);

    int addProductOrder(ProductOrder productOrder);

    void deleteRelationsByOrderId(int orderId);
}
