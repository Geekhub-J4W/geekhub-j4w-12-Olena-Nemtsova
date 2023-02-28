package edu.geekhub.homework.service;

import edu.geekhub.homework.domain.ProductOrder;
import edu.geekhub.homework.domain.ProductOrderValidator;
import edu.geekhub.homework.repository.interfaces.ProductOrderRepository;
import edu.geekhub.homework.service.interfaces.ProductOrderService;
import java.util.List;
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
        boolean successAdd = false;
        try {
            validator.validate(productOrder);
            productOrderRepository.addProductOrder(productOrder);
            successAdd = true;
        } catch (IllegalArgumentException exception) {
            Logger.warn("ProductOrder wasn't added: "
                        + productOrder + "\n"
                        + exception.getMessage());
        }
        return successAdd;
    }
}
