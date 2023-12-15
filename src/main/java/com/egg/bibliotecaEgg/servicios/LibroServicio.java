package com.egg.bibliotecaEgg.servicios;

import com.egg.bibliotecaEgg.entidades.Autor;
import com.egg.bibliotecaEgg.entidades.Editorial;
import com.egg.bibliotecaEgg.entidades.Libro;
import com.egg.bibliotecaEgg.excepciones.MiException;
import com.egg.bibliotecaEgg.repositorios.AutorRepositorio;
import com.egg.bibliotecaEgg.repositorios.EditorialRepositorio;
import com.egg.bibliotecaEgg.repositorios.LibroRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class LibroServicio {
    
    @Autowired
    private AutorRepositorio autorRepositorio;
    @Autowired
    private EditorialRepositorio editorialRepositorio;
    @Autowired
    private LibroRepositorio libroRepositorio;
    
    @Transactional
    public void crearLibro(Long isbn, String titulo, Integer ejemplares, String idAutor, String idEditorial) throws MiException    {
        validar(isbn, titulo, ejemplares, idAutor, idEditorial);
        Autor autor = autorRepositorio.findById(idAutor).get();
        Editorial editorial = editorialRepositorio.findById(idEditorial).get();
        
        Libro libro = new Libro();
        libro.setIsbn(isbn);
        libro.setTitulo(titulo);
        libro.setEjemplares(ejemplares);
        libro.setAlta(new Date());
        
        libro.setAutor(autor);
        libro.setEditorial(editorial);
        
        libroRepositorio.save(libro);
    }
    
    public Libro buscarPorIsbn(Long isbn)   {
        return libroRepositorio.buscarPorIsbn(isbn);
    }
    
    public List<Libro> listarLibros()   {
        List<Libro> listaLibros = new ArrayList();
        listaLibros = libroRepositorio.findAll();
        
        return listaLibros;
    }
    
    @Transactional
    public void modificarLibro(Long isbn, String titulo, Integer ejemplares, String idAutor, String idEditorial) throws MiException    {
        validar(isbn, titulo, ejemplares, idAutor, idEditorial);
        
        Optional<Libro> respuesta = libroRepositorio.findById(isbn);
        Optional<Autor> respuestaAutor = autorRepositorio.findById(idAutor);
        Optional<Editorial> respuestaEditorial = editorialRepositorio.findById(idEditorial);
        Autor autor = new Autor();
        Editorial editorial = new Editorial();
        if(respuestaAutor.isPresent())  {
            autor = respuestaAutor.get();
        }
        if(respuestaEditorial.isPresent())  {
            editorial = respuestaEditorial.get();
        }
        
        if(respuesta.isPresent())   {
            
            Libro libro = respuesta.get();
            
            libro.setTitulo(titulo);
            libro.setEjemplares(ejemplares);
            libro.setAutor(autor);
            libro.setEditorial(editorial);
            
            libroRepositorio.save(libro);
        }   
    }
    
    private void validar(Long isbn, String titulo, Integer ejemplares, String idAutor, String idEditorial) throws MiException  {
        
        if(isbn == null)    {
            throw new MiException("El ISBN no puede ser nulo");
        }
        if(titulo.isEmpty() || titulo == null)  {
            throw new MiException("El titulo no puede estar vacío");
        }
        if(ejemplares == null)  {
            throw new MiException("Los ejemplares no pueden ser nulos");
        }
        if(idAutor.isEmpty())    {
            throw new MiException("Debe seleccionar un autor");
        }
        if(idAutor.isEmpty())    {
            throw new MiException("Debe seleccionar una editorial");
        }
    }
}
