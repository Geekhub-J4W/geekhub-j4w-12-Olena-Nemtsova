package edu.geekhub.homework;

import edu.geekhub.homework.products.Product;
import edu.geekhub.homework.products.ProductsSortType;
import edu.geekhub.homework.products.interfaces.ProductService;
import java.io.IOException;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

    @GetMapping("/search/{input}")
    public Collection<Product> getSimilarNameProducts(@PathVariable String input) {
        return productService.getProductsNameContainsInput(input);
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable int id) {
        return productService.getProductById(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN', 'SELLER')")
    public Product addProduct(@RequestBody Product product) {
        Product newProduct = productService.addProduct(product);

        if (newProduct == null) {
            throw new IllegalArgumentException("Product wasn't added");
        }
        return newProduct;
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN', 'SELLER')")
    public Product editProduct(@RequestBody Product product,
                               @PathVariable(value = "id") int id) {

        Product updatedProduct = productService.updateProductById(product, id);
        if (updatedProduct == null) {
            throw new IllegalArgumentException("Product wasn't updated");
        }

        return updatedProduct;
    }

    @PostMapping("/setImage/{id}")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN', 'SELLER')")
    public Product setProductImage(@RequestParam("file") MultipartFile file,
                                   @PathVariable(value = "id") int id) throws IOException {
        Product product = productService.getProductById(id);
        if (product != null) {
            product.setImage(file.getBytes());
        }
        Product updatedProduct = productService.updateProductById(product, id);
        if (updatedProduct == null) {
            throw new IllegalArgumentException("Product image wasn't updated");
        }
        return updatedProduct;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN', 'SELLER')")
    public void deleteProduct(@PathVariable(value = "id") int id) {

        if (!productService.deleteProductById(id)) {
            throw new IllegalArgumentException("Product wasn't deleted");
        }
    }

    @GetMapping("/{sortType}/{categoryId}/{limit}/{pageNumber}")
    public Collection<Product> productsOfCategorySorted(
        @PathVariable(value = "categoryId") int categoryId,
        @PathVariable(value = "sortType") String sortType,
        @PathVariable int limit,
        @PathVariable int pageNumber) {

        return productService.getSortedProductsByCategoryWithPagination(
            ProductsSortType.valueOf(sortType),
            categoryId,
            limit,
            pageNumber);
    }

    @GetMapping("/pagesCount/{categoryId}/{limit}")
    public int getCountOfProductPages(@PathVariable(value = "categoryId") int categoryId,
                                      @PathVariable(value = "limit") int limit) {

        return productService.getCountOfPages(categoryId, limit);
    }
}
