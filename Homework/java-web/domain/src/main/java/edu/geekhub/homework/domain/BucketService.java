package edu.geekhub.homework.domain;

import java.time.LocalDateTime;
import java.util.List;
import org.tinylog.Logger;

public class BucketService {
    private final Bucket bucket;
    private final OrderRepository orderRepository;
    private final ProductService productService;

    public BucketService(ProductService productService,
                         OrderRepository orderRepository,
                         Bucket bucket) {
        this.productService = productService;
        this.orderRepository = orderRepository;
        this.bucket = bucket;
    }

    public boolean addProduct(Product product) {
        if (productService.containsProduct(product)
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
        Order order = new Order(bucket.getBucketProducts(), LocalDateTime.now());

        if (orderRepository.addOrder(order)) {
            bucket.clearBucket();
            Logger.info("Order was added to order repository");
            return true;
        }

        Logger.info("Order wasn't added to order repository");
        return false;
    }
}
