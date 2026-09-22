package com.nutrimax.msproductos.config;

import com.nutrimax.msproductos.model.Categoria;
import com.nutrimax.msproductos.repository.CategoriaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initCategorias(CategoriaRepository categoriaRepository) {
        return args -> {
            String[] categorias = { "Proteinas", "Creatina", "Pre-entrenos", "Vitaminas", "Accesorios" };

            for (String nombre : categorias) {
                if (categoriaRepository.findByNombre(nombre).isEmpty()) {
                    categoriaRepository.save(new Categoria(nombre));
                }
            }
        };
    }
}