package com.GreenEnergy.gestionUsuarios.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.GreenEnergy.gestionUsuarios.model.Administrador;
import com.GreenEnergy.gestionUsuarios.model.Cliente;
import com.GreenEnergy.gestionUsuarios.model.Rol;
import com.GreenEnergy.gestionUsuarios.model.Tecnico;
import com.GreenEnergy.gestionUsuarios.model.TecnicoSoporte;
import com.GreenEnergy.gestionUsuarios.model.Usuario;
import com.GreenEnergy.gestionUsuarios.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ----------------------REGISTRO----------------------

    public Usuario registrarCliente(Usuario usuario) {
        if (usuario.getNombre() == null || usuario.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }

        if (usuario.getApellido() == null || usuario.getApellido().isBlank()) {
            throw new IllegalArgumentException("El apellido es obligatorio.");
        }

        if (usuario.getRut() == null || usuario.getRut().isBlank()) {
            throw new IllegalArgumentException("El RUT es obligatorio.");
        }
        if (usuarioRepository.findByRut(usuario.getRut()).isPresent()) {
            throw new IllegalArgumentException("El RUT ya está registrado.");
        }

        if (usuario.getEmail() == null || usuario.getEmail().isBlank()) {
            throw new IllegalArgumentException("El email es obligatorio.");
        }

        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new IllegalArgumentException("El email ya está registrado.");
        }

        if (usuario.getTelefono() == null || usuario.getTelefono().isBlank()) {
            throw new IllegalArgumentException("El teléfono es obligatorio.");
        }

        if (usuario.getPassword() == null || usuario.getPassword().isBlank()) {
            throw new IllegalArgumentException("La contraseña es obligatoria.");
        }

        if (usuario.getPassword().length() <= 7) {
            throw new IllegalArgumentException("La contraseña debe tener más de 7 caracteres.");
        }

        usuario.setRol(Rol.CLIENTE);
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        Cliente cliente = new Cliente();
        cliente.setUsuario(usuario);
        usuario.setCliente(cliente);

        return usuarioRepository.save(usuario);
    }

    public Usuario registrarTecnico(Usuario usuario) {
        if (usuario.getNombre() == null || usuario.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }

        if (usuario.getApellido() == null || usuario.getApellido().isBlank()) {
            throw new IllegalArgumentException("El apellido es obligatorio.");
        }

        if (usuario.getRut() == null || usuario.getRut().isBlank()) {
            throw new IllegalArgumentException("El RUT es obligatorio.");
        }
        if (usuarioRepository.findByRut(usuario.getRut()).isPresent()) {
            throw new IllegalArgumentException("El RUT ya está registrado.");
        }

        if (usuario.getEmail() == null || usuario.getEmail().isBlank()) {
            throw new IllegalArgumentException("El email es obligatorio.");
        }

        if (usuario.getTelefono() == null || usuario.getTelefono().isBlank()) {
            throw new IllegalArgumentException("El teléfono es obligatorio.");
        }

        if (usuario.getPassword() == null || usuario.getPassword().isBlank()) {
            throw new IllegalArgumentException("La contraseña es obligatoria.");
        }

        if (usuario.getPassword().length() <= 7) {
            throw new IllegalArgumentException("La contraseña debe tener más de 7 caracteres.");
        }

        if (usuario.getTecnico() == null || usuario.getTecnico().getEspecialidad() == null) {
            throw new IllegalArgumentException("Debe especificar una especialidad.");
        }

        String especialidad = usuario.getTecnico().getEspecialidad().toLowerCase().trim();

        List<String> especialidadesValidas = List.of(
                "electricista",
                "instalador fotovoltaico",
                "instalador de estructura",
                "ayudante tecnico",
                "limpieza de paneles");

        if (!especialidadesValidas.contains(especialidad)) {
            throw new IllegalArgumentException(
                    "Especialidad inválida. Debe ser una de: " + String.join(", ", especialidadesValidas));
        }

        usuario.setRol(Rol.TECNICO);
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        Tecnico tecnico = new Tecnico();
        tecnico.setUsuario(usuario);
        tecnico.setEspecialidad(especialidad);
        usuario.setTecnico(tecnico);

        return usuarioRepository.save(usuario);
    }

    public Usuario registrarTecnicoSoporte(Usuario usuario) {
        if (usuario.getNombre() == null || usuario.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }

        if (usuario.getApellido() == null || usuario.getApellido().isBlank()) {
            throw new IllegalArgumentException("El apellido es obligatorio.");
        }

        if (usuario.getRut() == null || usuario.getRut().isBlank()) {
            throw new IllegalArgumentException("El RUT es obligatorio.");
        }
        if (usuarioRepository.findByRut(usuario.getRut()).isPresent()) {
            throw new IllegalArgumentException("El RUT ya está registrado.");
        }

        if (usuario.getEmail() == null || usuario.getEmail().isBlank()) {
            throw new IllegalArgumentException("El email es obligatorio.");
        }

        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new IllegalArgumentException("El email ya está registrado.");
        }

        if (usuario.getTelefono() == null || usuario.getTelefono().isBlank()) {
            throw new IllegalArgumentException("El teléfono es obligatorio.");
        }

        if (usuario.getPassword() == null || usuario.getPassword().isBlank()) {
            throw new IllegalArgumentException("La contraseña es obligatoria.");
        }

        if (usuario.getPassword().length() <= 7) {
            throw new IllegalArgumentException("La contraseña debe tener más de 7 caracteres.");
        }

        usuario.setRol(Rol.TECNICO_SOPORTE);
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        TecnicoSoporte tecnicoSoporte = new TecnicoSoporte();
        tecnicoSoporte.setUsuario(usuario);
        usuario.setTecnicoSoporte(tecnicoSoporte);

        return usuarioRepository.save(usuario);
    }

    public Usuario registrarAdministrador(Usuario usuario) {

        if (usuario.getNombre() == null || usuario.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }

        if (usuario.getApellido() == null || usuario.getApellido().isBlank()) {
            throw new IllegalArgumentException("El apellido es obligatorio.");
        }

        if (usuario.getRut() == null || usuario.getRut().isBlank()) {
            throw new IllegalArgumentException("El RUT es obligatorio.");
        }
        if (usuarioRepository.findByRut(usuario.getRut()).isPresent()) {
            throw new IllegalArgumentException("El RUT ya está registrado.");
        }

        if (usuario.getEmail() == null || usuario.getEmail().isBlank()) {
            throw new IllegalArgumentException("El email es obligatorio.");
        }

        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new IllegalArgumentException("El email ya está registrado.");
        }

        if (usuario.getTelefono() == null || usuario.getTelefono().isBlank()) {
            throw new IllegalArgumentException("El teléfono es obligatorio.");
        }

        if (usuario.getPassword() == null || usuario.getPassword().isBlank()) {
            throw new IllegalArgumentException("La contraseña es obligatoria.");
        }

        if (usuario.getPassword().length() <= 7) {
            throw new IllegalArgumentException("La contraseña debe tener más de 7 caracteres.");
        }

        usuario.setRol(Rol.ADMINISTRADOR);
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        Administrador administrador = new Administrador();
        administrador.setUsuario(usuario);
        usuario.setAdministrador(administrador);

        return usuarioRepository.save(usuario);
    }

    // ----------------------LOGIN----------------------

    public Usuario login(String email, String password) {

        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            throw new IllegalArgumentException("Debe ingresar email y contraseña.");
        }

        Usuario usuario = usuarioRepository.findByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new RuntimeException("Email no encontrado."));

        if (!passwordEncoder.matches(password, usuario.getPassword())) {
            throw new RuntimeException("Contraseña inválida.");
        }

        return usuario;
    }

    // ----------------------ACTUALIZAR----------------------

    public Usuario actualizarDatos(Long id, Usuario nuevosDatos) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (nuevosDatos.getNombre() != null && !nuevosDatos.getNombre().isBlank()) {
            usuario.setNombre(nuevosDatos.getNombre());
        }

        if (nuevosDatos.getApellido() != null && !nuevosDatos.getApellido().isBlank()) {
            usuario.setApellido(nuevosDatos.getApellido());
        }

        if (nuevosDatos.getTelefono() != null && !nuevosDatos.getTelefono().isBlank()) {
            usuario.setTelefono(nuevosDatos.getTelefono());
        }

        if (nuevosDatos.getEmail() != null && !nuevosDatos.getEmail().isBlank()) {
            String emailNuevo = nuevosDatos.getEmail().trim().toLowerCase();

            usuarioRepository.findByEmail(emailNuevo).ifPresent(otroUsuario -> {
                if (!otroUsuario.getId().equals(id)) {
                    throw new IllegalArgumentException("El email ya existe.");
                }
            });

            usuario.setEmail(emailNuevo);
        }

        if (nuevosDatos.getPassword() != null && !nuevosDatos.getPassword().isBlank()) {
            usuario.setPassword(passwordEncoder.encode(nuevosDatos.getPassword()));
        }

        return usuarioRepository.save(usuario);
    }

    // ----------------------ELIMINAR----------------------

    public void eliminarCuenta(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuarioRepository.delete(usuario);
    }

    // ----------------------OTROS----------------------

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public List<Usuario> listarPorRol(Rol rol) {
        return usuarioRepository.findAll()
                .stream()
                .filter(usuario -> usuario.getRol() == rol)
                .toList();
    }

}
