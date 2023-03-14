package edu.geekhub.homework;

import edu.geekhub.homework.domain.Product;
import edu.geekhub.homework.service.BucketService;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping("/add/{userId}/{productId}")
    public Collection<Product> addProductToUserBucket(
        @PathVariable(value = "productId") int productId,
        @PathVariable(value = "userId") String userId) {

        bucketService.addProductById(productId, userId);
        return bucketService.getBucketProducts(userId);
    }

    @DeleteMapping("/delete/{userId}/{productId}")
    public Collection<Product> deleteProductFromUserBucket(
        @PathVariable(value = "productId") int productId,
        @PathVariable(value = "userId") String userId) {

        bucketService.deleteProduct(productId, userId);
        return bucketService.getBucketProducts(userId);
    }

    @GetMapping("/{userId}")
    public Collection<Product> getBucketProducts(@PathVariable(value = "userId") String userId) {

        return bucketService.getBucketProducts(userId);
    }

    @GetMapping("/totalPrice/{userId}")
    public double getBucketTotalPrice(@PathVariable(value = "userId") String userId) {

        return bucketService.getBucketTotalPrice(userId);
    }

    @PostMapping("/checkout/{userId}")
    public String checkout(@PathVariable(value = "userId") String userId) {

        String checkContent = bucketService.checkout(userId);
        if (checkContent == null) {
            throw new IllegalArgumentException("Bucket wasn't checkout");
        }
        return checkContent;
    }
}
