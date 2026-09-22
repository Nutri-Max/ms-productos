package com.nutrimax.msproductos.controller;

import com.nutrimax.msproductos.model.Producto;
import com.nutrimax.msproductos.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    // RF-PR-03: catalogo completo
    @GetMapping
    public ResponseEntity<List<Producto>> listar() {
        return ResponseEntity.ok(productoService.listarActivos());
    }

    // RF-PR-03: filtro por categoria
    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<Producto>> filtrarPorCategoria(@PathVariable Long categoriaId) {
        return ResponseEntity.ok(productoService.filtrarPorCategoria(categoriaId));
    }

    // RF-PR-03: filtro por marca
    @GetMapping("/marca/{marca}")
    public ResponseEntity<List<Producto>> filtrarPorMarca(@PathVariable String marca) {
        return ResponseEntity.ok(productoService.filtrarPorMarca(marca));
    }

    // RF-PR-03: filtro por objetivo de entrenamiento
    @GetMapping("/objetivo/{objetivo}")
    public ResponseEntity<List<Producto>> filtrarPorObjetivo(@PathVariable String objetivo) {
        return ResponseEntity.ok(productoService.filtrarPorObjetivo(objetivo));
    }

    // RF-PR-03: busqueda por nombre
    @GetMapping("/buscar")
    public ResponseEntity<List<Producto>> buscarPorNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(productoService.buscarPorNombre(nombre));
    }

    // RF-PR-01: alta de producto
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Map<String, Object> payload) {
        try {
            Producto producto = new Producto();
            producto.setNombre((String) payload.get("nombre"));
            producto.setMarca((String) payload.get("marca"));
            producto.setSabor((String) payload.get("sabor"));
            producto.setPresentacion((String) payload.get("presentacion"));
            producto.setObjetivoEntrenamiento((String) payload.get("objetivoEntrenamiento"));
            producto.setPrecio(new java.math.BigDecimal(payload.get("precio").toString()));
            producto.setStock((Integer) payload.get("stock"));
            producto.setStockMinimo((Integer) payload.get("stockMinimo"));

            String nombreCategoria = (String) payload.get("categoria");

            Producto creado = productoService.crear(producto, nombreCategoria);
            return ResponseEntity.ok(creado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // RF-PR-01: edicion de producto
    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@PathVariable Long id, @RequestBody Producto producto) {
        try {
            return ResponseEntity.ok(productoService.editar(id, producto));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // RF-PR-01: desactivacion
    @DeleteMapping("/{id}")
    public ResponseEntity<?> desactivar(@PathVariable Long id) {
        try {
            productoService.desactivar(id);
            return ResponseEntity.ok(Map.of("mensaje", "Producto desactivado correctamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // RF-PR-02: descuento manual de stock (para pruebas; en produccion lo dispara
    // el evento OrderConfirmed)
    @PostMapping("/{id}/descontar-stock")
    public ResponseEntity<?> descontarStock(@PathVariable Long id, @RequestBody Map<String, Integer> payload) {
        try {
            productoService.descontarStock(id, payload.get("cantidad"));
            return ResponseEntity.ok(Map.of("mensaje", "Stock actualizado correctamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}