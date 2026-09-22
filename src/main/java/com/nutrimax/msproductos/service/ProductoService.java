package com.nutrimax.msproductos.service;

import com.nutrimax.msproductos.model.Categoria;
import com.nutrimax.msproductos.model.Producto;
import com.nutrimax.msproductos.repository.CategoriaRepository;
import com.nutrimax.msproductos.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    // RF-PR-01: alta de producto
    public Producto crear(Producto producto, String nombreCategoria) {
        Categoria categoria = categoriaRepository.findByNombre(nombreCategoria)
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada: " + nombreCategoria));

        producto.setCategoria(categoria);
        producto.setActivo(true);
        return productoRepository.save(producto);
    }

    // RF-PR-01: edicion de producto
    public Producto editar(Long id, Producto datosActualizados) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        producto.setNombre(datosActualizados.getNombre());
        producto.setMarca(datosActualizados.getMarca());
        producto.setSabor(datosActualizados.getSabor());
        producto.setPresentacion(datosActualizados.getPresentacion());
        producto.setObjetivoEntrenamiento(datosActualizados.getObjetivoEntrenamiento());
        producto.setPrecio(datosActualizados.getPrecio());
        producto.setStockMinimo(datosActualizados.getStockMinimo());

        return productoRepository.save(producto);
    }

    // RF-PR-01: desactivacion (no se borra de la base de datos)
    public void desactivar(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        producto.setActivo(false);
        productoRepository.save(producto);
    }

    // RF-PR-02: descuento automatico de stock al confirmarse un pago
    public void descontarStock(Long productoId, int cantidad) {
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (producto.getStock() < cantidad) {
            throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombre());
        }

        producto.setStock(producto.getStock() - cantidad);
        productoRepository.save(producto);

        if (producto.getStock() <= producto.getStockMinimo()) {
            // Aqui mas adelante se puede publicar un evento para MS-Notificaciones
            System.out.println("ALERTA: Producto '" + producto.getNombre() + "' por debajo del stock minimo");
        }
    }

    // RF-PR-03: catalogo completo
    public List<Producto> listarActivos() {
        return productoRepository.findByActivoTrue();
    }

    // RF-PR-03: filtro por categoria
    public List<Producto> filtrarPorCategoria(Long categoriaId) {
        return productoRepository.findByCategoriaIdAndActivoTrue(categoriaId);
    }

    // RF-PR-03: filtro por marca
    public List<Producto> filtrarPorMarca(String marca) {
        return productoRepository.findByMarcaContainingIgnoreCaseAndActivoTrue(marca);
    }

    // RF-PR-03: filtro por objetivo de entrenamiento
    public List<Producto> filtrarPorObjetivo(String objetivo) {
        return productoRepository.findByObjetivoEntrenamientoAndActivoTrue(objetivo);
    }

    // RF-PR-03: busqueda por nombre
    public List<Producto> buscarPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCaseAndActivoTrue(nombre);
    }

    // RF-PR-02: productos con alerta de stock bajo (para reportes/administracion)
    public List<Producto> obtenerProductosConStockBajo() {
        return productoRepository.findByStockLessThanEqual(100); // ajustar logica si es necesario
    }
}