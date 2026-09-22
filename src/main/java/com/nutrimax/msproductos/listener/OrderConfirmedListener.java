package com.nutrimax.msproductos.listener;

import com.nutrimax.msproductos.dto.OrderConfirmedEvent;
import com.nutrimax.msproductos.service.ProductoService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderConfirmedListener {

    @Autowired
    private ProductoService productoService;

    @RabbitListener(queues = "productos.order-confirmed")
    public void manejarOrderConfirmed(OrderConfirmedEvent evento) {
        System.out.println("Evento OrderConfirmed recibido. Pedido: " + evento.getPedidoId());

        for (OrderConfirmedEvent.ItemEvento item : evento.getItems()) {
            try {
                productoService.descontarStock(item.getProductoId(), item.getCantidad());
                System.out.println("Stock descontado: producto " + item.getProductoId()
                        + ", cantidad " + item.getCantidad());
            } catch (RuntimeException e) {
                System.out.println("Error al descontar stock del producto " + item.getProductoId()
                        + ": " + e.getMessage());
            }
        }
    }
}