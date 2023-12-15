package com.egg.bibliotecaEgg.servicios;

import com.egg.bibliotecaEgg.entidades.Editorial;
import com.egg.bibliotecaEgg.excepciones.MiException;
import com.egg.bibliotecaEgg.repositorios.EditorialRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EditorialServicio {
    
    @Autowired
    private EditorialRepositorio editorialRepositorio;
    
    @Transactional
    public void crearEditorial(String nombre) throws MiException   {
        
        validar(nombre);
        
        Editorial editorial = new Editorial();
        editorial.setNombre(nombre);
        editorialRepositorio.save(editorial);
        
    }
    
    public Editorial getOne(String id)  {
        return editorialRepositorio.getOne(id);
    }
    
    public List<Editorial> listarEditoriales()  {
        
        List<Editorial> listaEditoriales = new ArrayList();
        listaEditoriales = editorialRepositorio.findAll();
        
        return listaEditoriales;
        
    }
    
    @Transactional
    public void modificarEditorial(String nombre, String id) throws MiException    {
        
        validar(nombre);
        
        Optional<Editorial> respuesta = editorialRepositorio.findById(id);
        
        if(respuesta.isPresent())   {
            Editorial editorial = respuesta.get();
            editorial.setNombre(nombre);
            
            editorialRepositorio.save(editorial);
        }
    }
    
    public void validar(String nombre) throws MiException  {
        if(nombre.isEmpty() || nombre == null)  {
            throw new MiException("El nombre de la editorial no puede ser nulo");
        }
    }
    
}
