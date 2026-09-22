package com.nutrimax.msproductos.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class OrderConfirmedEvent implements Serializable {

    private Long pedidoId;
    private Long usuarioId;
    private BigDecimal total;
    private List<ItemEvento> items;

    public OrderConfirmedEvent() {
    }

    public Long getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Long pedidoId) {
        this.pedidoId = pedidoId;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public List<ItemEvento> getItems() {
        return items;
    }

    public void setItems(List<ItemEvento> items) {
        this.items = items;
    }

    public static class ItemEvento implements Serializable {
        private Long productoId;
        private Integer cantidad;

        public ItemEvento() {
        }

        public Long getProductoId() {
            return productoId;
        }

        public void setProductoId(Long productoId) {
            this.productoId = productoId;
        }

        public Integer getCantidad() {
            return cantidad;
        }

        public void setCantidad(Integer cantidad) {
            this.cantidad = cantidad;
        }
    }
}