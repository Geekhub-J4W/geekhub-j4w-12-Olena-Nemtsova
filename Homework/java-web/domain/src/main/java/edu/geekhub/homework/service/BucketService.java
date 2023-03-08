package edu.geekhub.homework.service;

import edu.geekhub.homework.domain.Order;
import edu.geekhub.homework.domain.Product;
import edu.geekhub.homework.domain.ProductOrder;
import edu.geekhub.homework.repository.interfaces.BucketRepository;
import edu.geekhub.homework.service.interfaces.OrderService;
import edu.geekhub.homework.service.interfaces.ProductOrderService;
import edu.geekhub.homework.service.interfaces.ProductService;
import java.time.LocalDateTime;
import java.util.List;
import org.tinylog.Logger;

public class BucketService {
    private final BucketRepository bucketRepository;
    private final OrderService orderService;
    private final ProductService productService;
    private final ProductOrderService productOrderService;

    public BucketService(ProductService productService,
                         OrderService orderService,
                         ProductOrderService productOrderService,
                         BucketRepository bucketRepository) {
        this.productService = productService;
        this.orderService = orderService;
        this.productOrderService = productOrderService;
        this.bucketRepository = bucketRepository;
    }

    public boolean addProduct(Product product, String userId) {
        if (productService.getProductById(product.getId()) != null
            && bucketRepository.addBucketProduct(product.getId(), userId) != -1) {
            Logger.info("Product was added to basket:\n" + product);
            return true;
        }
        Logger.warn("Product out of stock:\n" + product);
        return false;
    }

    public boolean addProductById(int productId, String userId) {
        Product product = productService.getProductById(productId);
        if (product != null
            && bucketRepository.addBucketProduct(productId, userId) != -1) {
            Logger.info("Product was added to basket:\n" + product);
            return true;
        }
        Logger.warn("Product out of stock:\n" + product);
        return false;
    }

    public List<Product> getBucketProducts(String userId) {
        return bucketRepository.getBucketProductsByUserId(userId);
    }

    public boolean deleteProduct(int productId, String userId) {
        if (bucketRepository.deleteUserBucketProductById(productId, userId) != 0) {
            Logger.info("Product with id '" + productId + "' was deleted from basket");
            return true;
        }
        Logger.warn("Product with id '" + productId + "' wasn't deleted from basket: not found");
        return false;
    }

    public double getBucketTotalPrice(String userId) {
        return bucketRepository.getBucketProductsByUserId(userId)
            .stream()
            .mapToDouble(Product::getPrice)
            .sum();
    }

    public String checkout(String userId) {
        if (bucketRepository.getBucketProductsByUserId(userId).isEmpty()) {
            Logger.warn("Can't checkout empty bucket!");
            return null;
        }
        Order order = new Order(LocalDateTime.now(), userId);
        order.setId(orderService.addOrder(order));

        if (order.getId() == -1) {
            return null;
        }

        if (!addProductsOrdersRelations(order, userId)) {
            orderService.deleteOrder(order.getId());
            return null;
        }

        double totalPrice = bucketRepository.getBucketProductsByUserId(userId).stream()
            .mapToDouble(Product::getPrice)
            .sum();
        orderService.updateOrderPriceById(totalPrice, order.getId());
        order.setTotalPrice(totalPrice);

        String checkContent = orderService.saveToFile(order,
            bucketRepository.getBucketProductsByUserId(userId)
        );

        bucketRepository.deleteUserBucketProductsById(userId);
        return checkContent;
    }

    private boolean addProductsOrdersRelations(Order order, String userId) {
        for (Product product : bucketRepository.getBucketProductsByUserId(userId)) {
            ProductOrder productOrder = new ProductOrder(product.getId(), order.getId());
            if (!productOrderService.addProductOrder(productOrder)) {
                return false;
            }
        }
        return true;
    }
}
