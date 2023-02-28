package edu.geekhub.homework.service;

import edu.geekhub.homework.domain.Bucket;
import edu.geekhub.homework.domain.Order;
import edu.geekhub.homework.domain.Product;
import edu.geekhub.homework.domain.ProductOrder;
import edu.geekhub.homework.service.interfaces.OrderService;
import edu.geekhub.homework.service.interfaces.ProductOrderService;
import edu.geekhub.homework.service.interfaces.ProductService;
import java.time.LocalDateTime;
import java.util.List;
import org.tinylog.Logger;

public class BucketService {
    private final Bucket bucket;
    private final OrderService orderService;
    private final ProductService productService;
    private final ProductOrderService productOrderService;

    public BucketService(ProductService productService,
                         OrderService orderService,
                         ProductOrderService productOrderService,
                         Bucket bucket) {
        this.productService = productService;
        this.orderService = orderService;
        this.productOrderService = productOrderService;
        this.bucket = bucket;
    }

    public boolean addProduct(Product product) {
        if (productService.getProductById(product.id()) != null
            && bucket.addProduct(product)) {
            Logger.info("Product was added to basket:\n" + product);
            return true;
        }
        Logger.warn("Product out of stock:\n" + product);
        return false;
    }

    public List<Product> getBucketProducts() {
        return bucket.getBucketProducts();
    }

    public boolean deleteProduct(int id) {
        if (bucket.deleteProduct(id)) {
            Logger.info("Product with id '" + id + "' was deleted from basket");
            return true;
        }
        Logger.warn("Product with id '" + id + "' wasn't deleted from basket: not found");
        return false;
    }

    public boolean checkout() {
        if (bucket.getBucketProducts().isEmpty()) {
            Logger.warn("Can't checkout empty bucket!");
            return false;
        }
        Order order = new Order(LocalDateTime.now());
        order = order.changeId(orderService.addOrder(order));

        if (order.id() == -1) {
            return false;
        }

        if (!addProductsOrdersRelations(order)) {
            orderService.deleteOrder(order.id());
            return false;
        }

        double totalPrice = bucket.getBucketProducts().stream()
            .mapToDouble(Product::price)
            .sum();
        orderService.updateOrderPriceById(totalPrice, order.id());
        orderService.saveToFile(order.changeTotalPrice(totalPrice));

        bucket.clearBucket();
        return true;
    }

    private boolean addProductsOrdersRelations(Order order) {
        for (Product product : bucket.getBucketProducts()) {
            ProductOrder productOrder = new ProductOrder(product.id(), order.id());
            if (!productOrderService.addProductOrder(productOrder)) {
                return false;
            }
        }
        return true;
    }
}
