package com.shop.market;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.shop.market.entities.Client;
import com.shop.market.entities.Order;
import com.shop.market.entities.OrderItem;
import com.shop.market.entities.Payment;
import com.shop.market.entities.PaymentMethod;
import com.shop.market.entities.Product;
import com.shop.market.entities.Status;


public class TestUtil {
    public static List<Product> genProduct(){
        List<Product> products = new ArrayList<>();
        products.add(Product.builder()
                        .name("mondongo")
                        .price(6900.00)
                        .stock(10)
                        .build()
        );

        products.add(Product.builder()
                    .name("Ibuprofeno")
                    .price(350.00)
                    .stock(5)
                    .build()
        );

        products.add(Product.builder()
                .name("Dulce de coco")
                .price(3999.99)
                .stock(0)
                .build()
        );
        
        products.add(Product.builder()
                .name("Mango")
                .price(700.49)
                .stock(7)
                .build()
        );

        products.add(Product.builder()
                .name("Arroz con leche")
                .price(2499.99)
                .stock(0)
                .build()
        );
        return products;
    }

    public static List<Client> genClients(){
        List<Client> clients = new ArrayList<>();
        clients.add(Client.builder()
                        .name("Gian Carlos Astori Goose")
                        .email("duck_destroyer89@gmail.com")
                        .address("Unimag Lake")
                        .build()
        );

        clients.add(Client.builder()
                        .name("Juan Camilo Horse Galvis")
                        .email("juannoscuida@gmail.com")
                        .address("stable")
                        .build()
        );

        clients.add(Client.builder()
                        .name("Hassan Mohammad Ali")
                        .email("imnotaterrorist@gmail.com")
                        .address("my house")
                        .build()
        );
        
        clients.add(Client.builder()
                        .name("Juan Village")
                        .email("anotherjuanbutpoorer@gmail.com")
                        .address("hill")
                        .build()
        );

        clients.add(Client.builder()
                        .name("Juan Luis Money Musk")
                        .email("mylastnameismoney@gmail.com")
                        .address("the moon")
                        .build()
        );
        return clients;
    }

    public static List<Order> genOrders(List<Client> clients){
        List<Order> orders = new ArrayList<>();
        orders.add(Order.builder()
                        .client(clients.get(0))
                        .timeOfOrder(LocalDateTime.now())
                        .status(Status.PENDING)
                        .build()
        );
        
        orders.add(Order.builder()
                        .client(clients.get(0))
                        .timeOfOrder(LocalDateTime.of(2022, 2, 10, 15, 30))
                        .status(Status.DELIVERED)
                        .build()
        );

        orders.add(Order.builder()
                        .client(clients.get(0))
                        .timeOfOrder(LocalDateTime.of(2024, 3, 26, 12, 12))
                        .status(Status.PENDING)
                        .build()
        );

        orders.add(Order.builder()
                .client(clients.get(3))
                .timeOfOrder(LocalDateTime.of(2020, 10, 25, 5, 40))
                .status(Status.DELIVERED)
                .build()
        );

        orders.add(Order.builder()
                .client(clients.get(2))
                .timeOfOrder(LocalDateTime.of(2024, 3, 20, 12, 10))
                .status(Status.SENT)
                .build()
        );

        orders.add(Order.builder()
                .client(clients.get(1))
                .timeOfOrder(LocalDateTime.of(2019, 12, 25, 23, 59))
                .status(Status.DELIVERED)
                .build()
        );

        orders.add(Order.builder()
                        .client(clients.get(1))
                        .timeOfOrder(LocalDateTime.of(2014, 3, 28, 7, 30))
                        .status(Status.PENDING)
                        .build()
        );
        return orders;
    }

    public static List<OrderItem> genOrderItems(List<Order> orders, List<Product> products){
        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(OrderItem.builder()
                            .order(orders.get(0))
                            .product(products.get(0))
                            .amount(100)
                            .pricePerUnit(products.get(0).getPrice())
                            .build()
        );
        
        orderItems.add(OrderItem.builder()
                            .order(orders.get(0))
                            .product(products.get(1))
                            .amount(150)
                            .pricePerUnit(products.get(1).getPrice())
                            .build()
        );

        orderItems.add(OrderItem.builder()
                            .order(orders.get(1))
                            .product(products.get(1))
                            .amount(5)
                            .pricePerUnit(products.get(1).getPrice())
                            .build()
        );

        orderItems.add(OrderItem.builder()
                            .order(orders.get(4))
                            .product(products.get(3))
                            .amount(10)
                            .pricePerUnit(products.get(3).getPrice())
                            .build()
        );

        orderItems.add(OrderItem.builder()
                            .order(orders.get(2))
                            .product(products.get(4))
                            .amount(4)
                            .pricePerUnit(products.get(4).getPrice())
                            .build()
        );

        orderItems.add(OrderItem.builder()
            .order(orders.get(2))
            .product(products.get(0))
            .amount(40)
            .pricePerUnit(products.get(0).getPrice())
            .build()
        );
        return orderItems;
    }

    public static List<Payment> genPayments(List<Order> orders){
        List<Payment> payments = new ArrayList<>();
        payments.add(Payment.builder()
                        .order(orders.get(1))
                        .totalPayment(5000.00)
                        .timeOfPayment(LocalDateTime.of(2022, 2, 10, 15, 50))
                        .paymentMethod(PaymentMethod.PSE)
                        .build()
        );
        payments.add(Payment.builder()
                        .order(orders.get(3))
                        .totalPayment(200.00)
                        .timeOfPayment(LocalDateTime.of(2020, 10, 27, 10, 10))
                        .paymentMethod(PaymentMethod.NEQUI)
                        .build()
        );

        payments.add(Payment.builder()
                        .order(orders.get(4))
                        .totalPayment(1500.02)
                        .timeOfPayment(LocalDateTime.of(2024, 3, 20, 14, 0))
                        .paymentMethod(PaymentMethod.PAYPAL)
                        .build()
        );

        payments.add(Payment.builder()
                .order(orders.get(5))
                .totalPayment(2000.12)
                .timeOfPayment(LocalDateTime.of(2019, 12, 25, 23, 59))
                .paymentMethod(PaymentMethod.PSE)
                .build()
        );

        payments.add(Payment.builder()
                        .order(orders.get(6))
                        .totalPayment(10000.23)
                        .timeOfPayment(LocalDateTime.of(2014, 3, 30, 15, 20))
                        .paymentMethod(PaymentMethod.CREDIT_CARD)
                        .build()
        );
        return payments;
    }
}
