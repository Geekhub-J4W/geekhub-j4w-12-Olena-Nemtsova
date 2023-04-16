package edu.geekhub.coursework;

import edu.geekhub.coursework.allergics.UserAllergicProduct;
import edu.geekhub.coursework.allergics.interfaces.UserAllergicProductService;
import edu.geekhub.coursework.security.SecurityUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/allergic")
public class UserAllergicProductController {
    private final UserAllergicProductService userAllergicProductService;

    public UserAllergicProductController(
        UserAllergicProductService userAllergicProductService
    ) {
        this.userAllergicProductService = userAllergicProductService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('USER')")
    public UserAllergicProduct addUserAllergicProduct(
        @RequestBody UserAllergicProduct userAllergicProduct
    ) {
        return userAllergicProductService.addRelation(userAllergicProduct);
    }

    @DeleteMapping("/{productId}")
    @PreAuthorize("hasAuthority('USER')")
    public boolean deleteUserAllergicProduct(@PathVariable int productId) {
        return userAllergicProductService.deleteRelationByUserAndProductId(
            getUserId(),
            productId
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
