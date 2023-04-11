package edu.geekhub.homework.productsorders.interfaces;

import edu.geekhub.homework.productsorders.ProductOrder;
import java.util.List;

public interface ProductOrderService {

    List<ProductOrder> getProductOrders();

    boolean addProductOrder(ProductOrder productOrder);

    boolean deleteRelationsByOrderId(int orderId);
}
