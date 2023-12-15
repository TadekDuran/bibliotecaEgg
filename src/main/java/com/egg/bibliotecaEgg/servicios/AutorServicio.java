package com.egg.bibliotecaEgg.servicios;

import com.egg.bibliotecaEgg.entidades.Autor;
import com.egg.bibliotecaEgg.excepciones.MiException;
import com.egg.bibliotecaEgg.repositorios.AutorRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AutorServicio {

    @Autowired
    private AutorRepositorio autorRepositorio;
    
    @Transactional
    public void crearAutor(String nombre) throws MiException   {
        
        validar(nombre);
        
        Autor autor = new Autor();
        autor.setNombre(nombre);
        
        autorRepositorio.save(autor);
        
    }
    
    public List<Autor> listarAutores()  {
        
        List<Autor> listaAutores = new ArrayList();
        listaAutores = autorRepositorio.findAll();
        
        return listaAutores;
    }
    
    @Transactional
    public void modificarAutor(String nombre, String id) throws MiException   {
        
        validar(nombre);
        
        Optional<Autor> respuesta = autorRepositorio.findById(id);
        
        if(respuesta.isPresent())   {
            Autor autor = respuesta.get();
            autor.setNombre(nombre);
            
            autorRepositorio.save(autor);
        }
    }
    
    public Autor getOne(String id)  {
        return autorRepositorio.getOne(id);
    }
    
    private void validar(String nombre) throws MiException  {
        if(nombre.isEmpty() || nombre == null)  {
            throw new MiException("El nombre del autor no puede ser nulo");
        }
    }
}
