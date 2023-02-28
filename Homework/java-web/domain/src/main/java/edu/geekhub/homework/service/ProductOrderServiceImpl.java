package edu.geekhub.homework.service;

import edu.geekhub.homework.domain.ProductOrder;
import edu.geekhub.homework.domain.ProductOrderValidator;
import edu.geekhub.homework.repository.interfaces.ProductOrderRepository;
import edu.geekhub.homework.service.interfaces.ProductOrderService;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.tinylog.Logger;

public class ProductOrderServiceImpl implements ProductOrderService {
    private final ProductOrderRepository productOrderRepository;
    private final ProductOrderValidator validator;

    public ProductOrderServiceImpl(ProductOrderRepository productOrderRepository,
                                   ProductOrderValidator validator) {
        this.productOrderRepository = productOrderRepository;
        this.validator = validator;
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
            return true;
        } catch (IllegalArgumentException | DataAccessException exception) {
            Logger.warn("ProductOrder wasn't added: "
                        + productOrder + "\n"
                        + exception.getMessage());
            return false;
        }
    }
}
