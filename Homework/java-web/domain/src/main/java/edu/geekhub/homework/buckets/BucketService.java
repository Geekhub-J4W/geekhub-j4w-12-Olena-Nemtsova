package edu.geekhub.homework.buckets;

import edu.geekhub.homework.orders.Order;
import edu.geekhub.homework.orders.interfaces.OrderService;
import edu.geekhub.homework.products.Product;
import edu.geekhub.homework.products.interfaces.ProductRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.tinylog.Logger;

@Service
public class BucketService {
    private final BucketRepository bucketRepository;
    private final ProductRepository productRepository;
    private final OrderService orderService;

    public BucketService(BucketRepository bucketRepository,
                         ProductRepository productRepository,
                         OrderService orderService) {

        this.bucketRepository = bucketRepository;
        this.productRepository = productRepository;
        this.orderService = orderService;
    }

    public boolean addProduct(int productId, int userId) {
        Product product = productRepository.getProductById(productId);
        if (product != null
            && product.getQuantity() > 0
            && bucketRepository.addBucketProduct(productId, userId) != -1) {
            Logger.info("Product was added to basket:\n" + product);
            return true;
        }
        Logger.warn("Product out of stock:\n" + product);
        return false;
    }

    public List<Product> getBucketProducts(int userId) {
        return bucketRepository.getBucketProductsByUserId(userId);
    }

    public boolean deleteAllConcreteProducts(int productId, int userId) {
        if (bucketRepository.deleteUserBucketProductById(productId, userId) != 0) {
            Logger.info("All products with id '" + productId
                        + "' was deleted from basket");
            return true;
        }
        Logger.warn("All products with id '" + productId
                    + "' wasn't deleted from basket: not found");
        return false;
    }

    public boolean deleteOneProduct(int productId, int userId) {
        if (bucketRepository.deleteUserBucketOneProductById(productId, userId) != 0) {
            Logger.info("Product with id '" + productId
                        + "' was deleted from basket");
            return true;
        }
        Logger.warn("Product with id '" + productId
                    + "' wasn't deleted from basket: not found");
        return false;
    }

    public double getBucketTotalPrice(int userId) {
        return bucketRepository.getBucketProductsByUserId(userId)
            .stream()
            .mapToDouble(Product::getPrice)
            .sum();
    }

    public boolean checkout(int userId, Order order) {
        if (order == null) {
            Logger.warn("Order was null");
            return false;
        }

        List<Product> bucketProducts = bucketRepository.getBucketProductsByUserId(userId);
        if (bucketProducts.isEmpty()) {
            Logger.warn("Can't checkout empty bucket!");
            return false;
        }

        order.setUserId(userId);
        order = orderService.addOrder(order);
        if (order == null) {
            return false;
        }

        if (!orderService.addProductsOfOrder(bucketProducts, order.getId())) {
            orderService.deleteOrder(order.getId());
            return false;
        }

        bucketRepository.deleteUserBucketProductsById(userId);
        return true;
    }

    public int getCountOfConcreteProductAtBucket(int productId, int userId) {
        return bucketRepository.getBucketProductsByUserId(userId).stream()
            .filter(product -> product.getId() == productId)
            .toList()
            .size();
    }
}
