package com.nutrimax.msproductos.repository;

import com.nutrimax.msproductos.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByActivoTrue();

    List<Producto> findByCategoriaIdAndActivoTrue(Long categoriaId);

    List<Producto> findByMarcaContainingIgnoreCaseAndActivoTrue(String marca);

    List<Producto> findByObjetivoEntrenamientoAndActivoTrue(String objetivo);

    List<Producto> findByNombreContainingIgnoreCaseAndActivoTrue(String nombre);

    List<Producto> findByStockLessThanEqual(Integer stockMinimo);
}