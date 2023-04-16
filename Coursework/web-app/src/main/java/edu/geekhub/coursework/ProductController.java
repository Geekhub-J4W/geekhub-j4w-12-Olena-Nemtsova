package edu.geekhub.coursework;

import edu.geekhub.coursework.products.Product;
import edu.geekhub.coursework.products.interfaces.ProductService;
import edu.geekhub.coursework.security.SecurityUser;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN')")
    public Product addProduct(@RequestBody Product product) {
        return productService.addProduct(product);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN')")
    public Product updateProduct(
        @RequestBody Product product,
        @PathVariable int id
    ) {
        return productService.updateProductById(product, id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN')")
    public boolean deleteProduct(@PathVariable int id) {
        return productService.deleteProductById(id);
    }

    @GetMapping("/pages/{limit}/{input}")
    public int getCountOfProductsPages(
        @PathVariable int limit,
        @PathVariable String input
    ) {
        return productService.getCountOfPages(limit, input);
    }

    @GetMapping("/{limit}/{pageNumber}/{input}")
    public Collection<Product> getProductsOfPageByInput(
        @PathVariable int limit,
        @PathVariable int pageNumber,
        @PathVariable String input
    ) {
        return productService.getProductsNameSortedByPageAndInput(limit, pageNumber, input);
    }

    @GetMapping("/dish/{dishId}")
    public Collection<Product> getProductsOfDish(@PathVariable int dishId) {
        return productService.getProductsOfDish(dishId);
    }

    @GetMapping("/allergic")
    @PreAuthorize("hasAuthority('USER')")
    public Collection<Product> getUserAllergicProducts() {
        return productService.getUserAllergicProducts(
            getUserId()
        );
    }

    private int getUserId() {
        Object object = SecurityContextHolder
            .getContext()
            .getAuthentication()
            .getPrincipal();

        if (object.toString().equals("anonymousUser")) {
            return -1;
        }
        SecurityUser user = (SecurityUser) object;
        return user.getUserId();
    }
}
