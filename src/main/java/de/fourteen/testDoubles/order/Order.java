package de.fourteen.testDoubles.order;

import java.util.Collection;
import java.util.List;

public record Order(int id, int customerId, Collection<OrderPosition> orderPositions) {
    public Order(int orderId, int customerId, OrderPosition... orderPositions) {
        this(orderId, customerId, List.of(orderPositions));
    }
}
