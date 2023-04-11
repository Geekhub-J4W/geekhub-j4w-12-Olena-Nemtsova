package edu.geekhub.homework;

import edu.geekhub.homework.buckets.BucketService;
import edu.geekhub.homework.orders.Order;
import edu.geekhub.homework.products.Product;
import edu.geekhub.homework.security.SecurityUser;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bucket")
public class BucketController {
    private final BucketService bucketService;

    @Autowired
    public BucketController(BucketService bucketService) {
        this.bucketService = bucketService;
    }

    @PostMapping("/add/{productId}")
    public Collection<Product> addProductToUserBucket(
        @PathVariable(value = "productId") int productId) {
        int userId = getUserId();
        bucketService.addProduct(productId, userId);
        return bucketService.getBucketProducts(userId);
    }

    @DeleteMapping("/delete/{productId}")
    public Collection<Product> deleteAllConcreteProductsFromUserBucket(
        @PathVariable(value = "productId") int productId) {
        int userId = getUserId();
        bucketService.deleteAllConcreteProducts(productId, userId);
        return bucketService.getBucketProducts(userId);
    }

    @DeleteMapping("/deleteOne/{productId}")
    public Collection<Product> deleteOneProductFromUserBucket(
        @PathVariable(value = "productId") int productId) {
        int userId = getUserId();

        bucketService.deleteOneProduct(productId, userId);
        return bucketService.getBucketProducts(userId);
    }

    @GetMapping
    public Collection<Product> getBucketProducts() {
        return bucketService.getBucketProducts(getUserId());
    }

    @GetMapping("/totalPrice")
    public double getBucketTotalPrice() {
        return bucketService.getBucketTotalPrice(getUserId());
    }

    @GetMapping("/quantity/{productId}")
    public int getQuantityOfConcreteProductAtBucket(
        @PathVariable(value = "productId") int productId) {
        return bucketService.getCountOfConcreteProductAtBucket(productId, getUserId());
    }

    @PostMapping("/checkout")
    public void checkout(@RequestBody Order order) {
        if (!bucketService.checkout(getUserId(), order)) {
            throw new IllegalArgumentException("Bucket wasn't checkout");
        }
    }

    private int getUserId() {
        SecurityUser user = (SecurityUser) SecurityContextHolder
            .getContext()
            .getAuthentication()
            .getPrincipal();
        return user.getUserId();
    }
}
