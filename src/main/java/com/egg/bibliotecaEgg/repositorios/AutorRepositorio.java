package com.egg.bibliotecaEgg.repositorios;

import com.egg.bibliotecaEgg.entidades.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AutorRepositorio extends JpaRepository<Autor, String>{
    
    
    
}
