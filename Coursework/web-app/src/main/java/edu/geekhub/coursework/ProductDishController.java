package edu.geekhub.coursework;

import edu.geekhub.coursework.productsdishes.ProductDish;
import edu.geekhub.coursework.productsdishes.interfaces.ProductDishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/productsDishes")
public class ProductDishController {
    private final ProductDishService productDishService;

    @Autowired
    public ProductDishController(ProductDishService productDishService) {
        this.productDishService = productDishService;
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN')")
    public ProductDish addRelation(@RequestBody ProductDish productDish) {
        return productDishService.addRelation(productDish);
    }

    @PutMapping
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN')")
    public ProductDish updateRelation(
        @RequestBody ProductDish productDish
    ) {
        return productDishService.updateRelation(productDish);
    }

    @DeleteMapping("/{productId}/{dishId}")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN')")
    public boolean deleteRelation(
        @PathVariable int productId,
        @PathVariable int dishId
    ) {
        return productDishService.deleteRelationByProductAndDishId(productId, dishId);
    }

    @GetMapping("/{productId}/{dishId}")
    public ProductDish getProductDish(
        @PathVariable int productId,
        @PathVariable int dishId
    ) {
        return productDishService.getRelationByProductAndDishId(productId, dishId);
    }
}
