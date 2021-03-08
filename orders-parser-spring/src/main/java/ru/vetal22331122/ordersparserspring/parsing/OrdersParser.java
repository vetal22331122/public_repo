package ru.vetal22331122.ordersparserspring.parsing;

import ru.vetal22331122.ordersparserspring.model.Order;

public abstract class OrdersParser implements Runnable {

    protected static volatile int orderQueuedNumber = 0;

    protected static synchronized void printOrder(Order order) {
        orderQueuedNumber = orderQueuedNumber + 1;
        String output = "{\"id\":" + orderQueuedNumber + ", "
                + "\"amount\":" + order.getFormattedAmount() + ","
                + "\"comment\":\"" + order.getComment() + "\","
                + "\"filename\":\"" + order.getFromFile() + "\","
                + "\"line\":" + order.getFoundOnLine() + ","
                + "\"result\":\"" + order.getResult() + "\","
                + "}";
        System.out.println(output);
    }

}
