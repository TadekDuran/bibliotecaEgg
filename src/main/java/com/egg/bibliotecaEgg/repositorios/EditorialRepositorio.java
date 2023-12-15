package com.egg.bibliotecaEgg.repositorios;

import com.egg.bibliotecaEgg.entidades.Editorial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EditorialRepositorio extends JpaRepository<Editorial, String>{
   
    
    
}
