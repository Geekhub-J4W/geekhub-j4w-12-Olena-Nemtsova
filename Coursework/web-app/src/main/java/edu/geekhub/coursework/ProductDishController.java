package edu.geekhub.coursework;

import edu.geekhub.coursework.productsdishes.ProductDish;
import edu.geekhub.coursework.productsdishes.interfaces.ProductDishService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/productsDishes")
public class ProductDishController {
    private final ProductDishService productDishService;

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

}
