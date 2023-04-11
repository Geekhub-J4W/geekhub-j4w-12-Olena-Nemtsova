package edu.geekhub.homework.productsorders;

import edu.geekhub.homework.products.Product;
import edu.geekhub.homework.products.interfaces.ProductService;
import edu.geekhub.homework.productsorders.interfaces.ProductOrderRepository;
import edu.geekhub.homework.productsorders.interfaces.ProductOrderService;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.tinylog.Logger;

@Service
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
            Logger.warn("ProductOrder relation wasn't added: "
                        + productOrder + "\n"
                        + exception.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteRelationsByOrderId(int orderId) {
        try {
            validator.validateOrderExists(orderId);
            List<Integer> productsId = productOrderRepository.getProductOrders().stream()
                .filter(relation -> relation.orderId() == orderId)
                .map(ProductOrder::productId)
                .toList();

            productOrderRepository.deleteRelationsByOrderId(orderId);

            for (int productId : productsId) {
                Product product = productService.getProductById(productId);
                product.setQuantity(product.getQuantity() + 1);
                productService.updateProductById(product, productId);
            }

            Logger.info("ProductOrder relations was deleted by order id: " + orderId);
            return true;
        } catch (IllegalArgumentException | DataAccessException exception) {
            Logger.warn("ProductOrder relations wasn't deleted by order id: "
                        + exception.getMessage());
            return false;
        }
    }
}
