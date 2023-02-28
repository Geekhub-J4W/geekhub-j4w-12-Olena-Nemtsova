package edu.geekhub.homework.service.interfaces;

import edu.geekhub.homework.domain.ProductOrder;
import java.util.List;

public interface ProductOrderService {

    List<ProductOrder> getProductOrders();

    boolean addProductOrder(ProductOrder productOrder);
}
