package com.samplecodes.base;


public interface Constants {
    static final String APPLICATION_CONTEXT_SOURCE = "/com/samplecodes/application-context.xml";
    static final String USER_QUERY_NAME = "find_user";
    static final String USER_QUERY = "select u from User u where u.userId.userName = ? and u.password = ?";
    static final String SHIPMENT_ORDER_BY_FIELD = "deliveryDate";
    static final String DRIVER_FOREIGN_KEY = "driver";
    static final String ORDERS_ORDER_BY_FIELD = "orderDate";
    static final String CUSTOMER_FOREIGN_KEY_FIELD = "customer";

}
