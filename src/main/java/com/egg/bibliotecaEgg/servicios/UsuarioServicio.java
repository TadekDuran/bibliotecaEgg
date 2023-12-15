package com.egg.bibliotecaEgg.servicios;

import com.egg.bibliotecaEgg.entidades.Imagen;
import com.egg.bibliotecaEgg.entidades.Usuario;
import com.egg.bibliotecaEgg.enumeraciones.Rol;
import com.egg.bibliotecaEgg.excepciones.MiException;
import com.egg.bibliotecaEgg.repositorios.UsuarioRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UsuarioServicio implements UserDetailsService {
    
    @Autowired
    UsuarioRepositorio usuarioRepositorio;
    @Autowired
    ImagenServicio imagenServicio;
    
    @Transactional
    public void registrar(MultipartFile archivo, String nombre, String email, String password, String password2) throws MiException   {
        validar(nombre, email, password, password2);
        Imagen imagen = imagenServicio.guardar(archivo);
        Usuario usuario = new Usuario(nombre, email, new BCryptPasswordEncoder().encode(password), Rol.USER, imagen);
        usuarioRepositorio.save(usuario);
    }
    
    @Transactional
    public void actualizar(MultipartFile archivo, String id, String nombre, String email, String password, String password2) throws MiException {
        validar(nombre, email, password, password2);
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if(respuesta.isPresent())   {
            Usuario usuario = respuesta.get();
            usuario.setNombre(nombre);
            usuario.setEmail(email);
            usuario.setPassword(new BCryptPasswordEncoder().encode(password));
            if(!archivo.isEmpty())  {
                String idImagen = null;
                if(usuario.getImagen() != null) {
                    idImagen = usuario.getImagen().getId();
                }
                Imagen imagen = imagenServicio.actualizar(archivo, idImagen);
                usuario.setImagen(imagen);
            }
            usuarioRepositorio.save(usuario);
        }
    }
    
    public Usuario getOne(String id)    {
        return usuarioRepositorio.getOne(id);
    }
    
    public void validar(String nombre, String email, String password, String password2) throws MiException {
        if(nombre.isEmpty())    {
            throw new MiException("El nombre no puede estar vacio");
        }
        if(email.isEmpty())    {
            throw new MiException("El email no puede estar vacio");
        }
        if(password.isEmpty() || password.length() <= 5)    {
            throw new MiException("La contraseña debe tener un minimo de 6 caracteres");
        }
        if(!password.equals(password2))    {
            throw new MiException("Las contraseñas deben ser iguales");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepositorio.buscarPorEmail(email);
        if(usuario != null) {
            List<GrantedAuthority> permisos = new ArrayList();
            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toString());
            permisos.add(p);
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("usuariosession", usuario);
            return new User(usuario.getEmail(), usuario.getPassword(), permisos);
        }   else    {
            return null;
        }
    }
}
