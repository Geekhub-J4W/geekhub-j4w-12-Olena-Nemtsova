package edu.geekhub.homework;

import edu.geekhub.homework.domain.Product;
import edu.geekhub.homework.domain.ProductsSortType;
import edu.geekhub.homework.service.interfaces.ProductService;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    @GetMapping
    public Collection<Product> getAllProducts() {
        return productService.getProducts();
    }

    @PostMapping("/newProduct")
    public Product addProduct(@RequestBody Product product) {
        Product newProduct = productService.addProduct(product);

        if (newProduct == null) {
            throw new IllegalArgumentException("Product wasn't added");
        }
        return newProduct;
    }

    @PostMapping("editProduct/{id}")
    public Product editProduct(@RequestBody Product product,
                               @PathVariable(value = "id") int id) {

        Product updatedProduct = productService.updateProductById(product, id);
        if (updatedProduct == null) {
            throw new IllegalArgumentException("Product wasn't updated");
        }
        return updatedProduct;
    }

    @DeleteMapping(value = "/deleteProduct/{id}")
    public void deleteProduct(@PathVariable(value = "id") int id) {

        if (!productService.deleteProductById(id)) {
            throw new IllegalArgumentException("Product wasn't deleted");
        }
    }

    @GetMapping("/{categoryId}")
    public Collection<Product> productsOfCategory(@PathVariable(value = "categoryId") int id) {

        return productService.getSortedProducts(ProductsSortType.RATING, id);
    }

    @GetMapping("/{sortType}/{categoryId}")
    public Collection<Product> productsSorted(@PathVariable(value = "categoryId") int categoryId,
                                              @PathVariable(value = "sortType") String sortType) {

        return productService
            .getSortedProducts(ProductsSortType.valueOf(sortType), categoryId);
    }
}
