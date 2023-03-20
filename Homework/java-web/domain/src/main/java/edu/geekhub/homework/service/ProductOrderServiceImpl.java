package edu.geekhub.homework.service;

import edu.geekhub.homework.domain.Product;
import edu.geekhub.homework.domain.ProductOrder;
import edu.geekhub.homework.domain.ProductOrderValidator;
import edu.geekhub.homework.repository.interfaces.ProductOrderRepository;
import edu.geekhub.homework.service.interfaces.ProductOrderService;
import edu.geekhub.homework.service.interfaces.ProductService;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.tinylog.Logger;

public class ProductOrderServiceImpl implements ProductOrderService {
    private final ProductOrderRepository productOrderRepository;
    private final ProductOrderValidator validator;
    private final ProductService productService;

    public ProductOrderServiceImpl(ProductOrderRepository productOrderRepository,
                                   ProductOrderValidator validator,
                                   ProductService productService) {
        this.productOrderRepository = productOrderRepository;
        this.validator = validator;
        this.productService = productService;
    }

    @Override
    public List<ProductOrder> getProductOrders() {
        return productOrderRepository.getProductOrders();
    }

    @Override
    public boolean addProductOrder(ProductOrder productOrder) {
        try {
            validator.validate(productOrder);
            int newRelationId = productOrderRepository.addProductOrder(productOrder);
            if (newRelationId == -1) {
                throw new IllegalArgumentException("Unable to retrieve the generated key");
            }

            Product product = productService.getProductById(productOrder.productId());
            product.setQuantity(product.getQuantity() - 1);
            productService.updateProductById(product, productOrder.productId());

            return true;
        } catch (IllegalArgumentException | DataAccessException exception) {
            Logger.warn("ProductOrder wasn't added: "
                        + productOrder + "\n"
                        + exception.getMessage());
            return false;
        }
    }
}
