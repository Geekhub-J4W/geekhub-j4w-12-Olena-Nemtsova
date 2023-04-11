package edu.geekhub.homework.orders;

import edu.geekhub.homework.orders.interfaces.OrderRepository;
import edu.geekhub.homework.orders.interfaces.OrderService;
import edu.geekhub.homework.products.Product;
import edu.geekhub.homework.productsorders.ProductOrder;
import edu.geekhub.homework.productsorders.interfaces.ProductOrderService;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.tinylog.Logger;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductOrderService productOrderService;
    private final OrderValidator validator;

    public OrderServiceImpl(OrderRepository orderRepository,
                            ProductOrderService productOrderService,
                            OrderValidator validator) {
        this.orderRepository = orderRepository;
        this.productOrderService = productOrderService;
        this.validator = validator;
    }

    @Override
    public List<Order> getOrders() {
        return orderRepository.getOrders();
    }

    @Override
    public List<Order> getOrdersByUserId(int userId) {
        return orderRepository.getOrdersByUserId(userId);
    }

    @Override
    public Order addOrder(Order order) {
        try {
            validator.validate(order);
            int newOrderId = orderRepository.addOrder(order);
            if (newOrderId == -1) {
                throw new IllegalArgumentException("Unable to retrieve the generated key");
            }
            Logger.info("Order was added:\n" + order);
            return getOrderById(newOrderId);
        } catch (IllegalArgumentException | DataAccessException exception) {
            Logger.warn("Order wasn't added: " + order + "\n" + exception.getMessage());
            return null;
        }
    }

    @Override
    public boolean deleteOrder(int id) {
        Order orderToDel = getOrderById(id);
        try {
            if (orderToDel == null) {
                throw new IllegalArgumentException("Order with id '" + id + "' not found");
            }
            orderRepository.deleteOrderById(id);
            return true;
        } catch (IllegalArgumentException | DataAccessException exception) {
            Logger.warn("Order wasn't deleted: " + orderToDel + "\n" + exception.getMessage());
            return false;
        }
    }

    @Override
    public Order getOrderById(int id) {
        return orderRepository.getOrderById(id);
    }

    @Override
    public List<Product> getOrderProducts(int id) {
        return orderRepository.getOrderProducts(id);
    }


    @Override
    public boolean updateOrderPriceById(double newPrice, int id) {
        Order orderToEdit = getOrderById(id);
        try {
            validator.validateTotalPrice(newPrice);
            if (orderToEdit == null) {
                throw new IllegalArgumentException("Order with id '" + id + "' not found");
            }

            orderRepository.updateOrderPriceById(newPrice, id);
            Logger.info("Order price was updated: "
                        + orderToEdit + "\n"
                        + "new price: " + newPrice);
            return true;
        } catch (IllegalArgumentException | DataAccessException exception) {
            Logger.warn("Order wasn't edited: " + orderToEdit + "\n" + exception.getMessage());
            return false;
        }
    }

    @Override
    public Order updateOrderById(Order order, int id) {
        Order orderToEdit = getOrderById(id);
        try {
            if (orderToEdit == null) {
                throw new IllegalArgumentException("Order with id '" + id + "' not found");
            }
            validator.validate(order);

            orderRepository.updateOrderById(order, id);
            Logger.info("Order was updated: "
                        + orderToEdit + "\n"
                        + "to: " + order);
            return getOrderById(id);
        } catch (IllegalArgumentException | DataAccessException exception) {
            Logger.warn("Order wasn't updated: " + orderToEdit + "\n" + exception.getMessage());
            return null;
        }
    }

    @Override
    public int getQuantityOfProductAtOrder(int productId, int orderId) {
        return orderRepository.getQuantityOfProductAtOrder(productId, orderId);
    }

    @Override
    public boolean addProductsOfOrder(List<Product> products, int id) {
        if (products == null || products.isEmpty()) {
            Logger.warn("Products not added to order: products was null or empty");
            return false;
        }

        for (Product product : products) {
            ProductOrder productOrder = new ProductOrder(product.getId(), id);
            if (!productOrderService.addProductOrder(productOrder)) {
                productOrderService.deleteRelationsByOrderId(id);
                return false;
            }
        }

        double totalPrice = getOrderProducts(id).stream()
            .mapToDouble(Product::getPrice)
            .sum();

        if (!updateOrderPriceById(totalPrice, id)) {
            productOrderService.deleteRelationsByOrderId(id);
            return false;
        }
        return true;
    }
}
