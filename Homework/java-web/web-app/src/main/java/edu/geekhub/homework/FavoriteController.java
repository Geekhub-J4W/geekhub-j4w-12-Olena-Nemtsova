package edu.geekhub.homework;

import edu.geekhub.homework.favorites.Favorite;
import edu.geekhub.homework.favorites.interfaces.FavoriteService;
import edu.geekhub.homework.products.Product;
import edu.geekhub.homework.security.SecurityUser;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {
    private final FavoriteService favoriteService;

    @Autowired
    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/{productId}")
    @PreAuthorize("hasAuthority('USER')")
    public Favorite addFavorite(@PathVariable int productId) {
        return favoriteService.addFavorite(new Favorite(getUserId(), productId));
    }

    @DeleteMapping("/{productId}")
    @PreAuthorize("hasAuthority('USER')")
    public void deleteFavorite(@PathVariable int productId) {
        favoriteService.deleteFavoriteByProductAndUserId(productId, getUserId());
    }

    @GetMapping
    @PreAuthorize("hasAuthority('USER')")
    public List<Product> getFavoriteUserProducts() {
        return favoriteService.getFavoriteUserProducts(getUserId());
    }

    @GetMapping("/{productId}")
    @PreAuthorize("hasAuthority('USER')")
    public boolean favoriteExists(@PathVariable int productId) {
        return favoriteService.favoriteExits(productId, getUserId());
    }

    private int getUserId() {
        SecurityUser user = (SecurityUser) SecurityContextHolder
            .getContext()
            .getAuthentication()
            .getPrincipal();
        return user.getUserId();
    }
}
