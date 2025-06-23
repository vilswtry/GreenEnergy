package com.GreenEnergy.gestionUsuarios.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import com.GreenEnergy.gestionUsuarios.model.*;
import com.GreenEnergy.gestionUsuarios.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuarioBase;

    @BeforeEach
    void setup() {
        usuarioBase = new Usuario();
        usuarioBase.setId(1L);
        usuarioBase.setNombre("Juan");
        usuarioBase.setApellido("Perez");
        usuarioBase.setRut("12345678-9");
        usuarioBase.setEmail("juan@example.com");
        usuarioBase.setTelefono("+56912345678");
        usuarioBase.setPassword("password123");
    }


    @Test
    void registrarCliente_ok() {
        usuarioBase.setRol(null);
        when(usuarioRepository.findByRut(usuarioBase.getRut())).thenReturn(Optional.empty());
        when(usuarioRepository.findByEmail(usuarioBase.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(any())).thenReturn("encodedPass");
        when(usuarioRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Usuario registrado = usuarioService.registrarCliente(usuarioBase);

        assertThat(registrado.getRol()).isEqualTo(Rol.CLIENTE);
        assertThat(registrado.getPassword()).isEqualTo("encodedPass");
        assertThat(registrado.getCliente()).isNotNull();
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void registrarCliente_nombreObligatorio() {
        usuarioBase.setNombre(" ");
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> usuarioService.registrarCliente(usuarioBase));
        assertThat(ex.getMessage()).isEqualTo("El nombre es obligatorio.");
    }

    @Test
    void registrarCliente_rutDuplicado() {
        when(usuarioRepository.findByRut(usuarioBase.getRut())).thenReturn(Optional.of(new Usuario()));
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> usuarioService.registrarCliente(usuarioBase));
        assertThat(ex.getMessage()).isEqualTo("El RUT ya está registrado.");
    }


    @Test
    void registrarTecnico_ok() {
        usuarioBase.setRol(null);
        Tecnico tecnico = new Tecnico();
        tecnico.setEspecialidad("electricista");
        usuarioBase.setTecnico(tecnico);

        when(usuarioRepository.findByRut(usuarioBase.getRut())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(any())).thenReturn("encodedPass");
        when(usuarioRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Usuario registrado = usuarioService.registrarTecnico(usuarioBase);

        assertThat(registrado.getRol()).isEqualTo(Rol.TECNICO);
        assertThat(registrado.getPassword()).isEqualTo("encodedPass");
        assertThat(registrado.getTecnico()).isNotNull();
        assertThat(registrado.getTecnico().getEspecialidad()).isEqualTo("electricista");
    }

    @Test
    void registrarTecnico_especialidadInvalida() {
        Tecnico tecnico = new Tecnico();
        tecnico.setEspecialidad("invalida");
        usuarioBase.setTecnico(tecnico);
        when(usuarioRepository.findByRut(usuarioBase.getRut())).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> usuarioService.registrarTecnico(usuarioBase));
        assertThat(ex.getMessage()).startsWith("Especialidad inválida");
    }


    @Test
    void registrarTecnicoSoporte_ok() {
        usuarioBase.setRol(null);
        when(usuarioRepository.findByRut(usuarioBase.getRut())).thenReturn(Optional.empty());
        when(usuarioRepository.findByEmail(usuarioBase.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(any())).thenReturn("encodedPass");
        when(usuarioRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Usuario registrado = usuarioService.registrarTecnicoSoporte(usuarioBase);

        assertThat(registrado.getRol()).isEqualTo(Rol.TECNICO_SOPORTE);
        assertThat(registrado.getPassword()).isEqualTo("encodedPass");
        assertThat(registrado.getTecnicoSoporte()).isNotNull();
    }


    @Test
    void registrarAdministrador_ok() {
        usuarioBase.setRol(null);
        when(usuarioRepository.findByRut(usuarioBase.getRut())).thenReturn(Optional.empty());
        when(usuarioRepository.findByEmail(usuarioBase.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(any())).thenReturn("encodedPass");
        when(usuarioRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Usuario registrado = usuarioService.registrarAdministrador(usuarioBase);

        assertThat(registrado.getRol()).isEqualTo(Rol.ADMINISTRADOR);
        assertThat(registrado.getPassword()).isEqualTo("encodedPass");
        assertThat(registrado.getAdministrador()).isNotNull();
    }


    @Test
    void login_ok() {
        usuarioBase.setPassword("encodedPass");
        when(usuarioRepository.findByEmail(usuarioBase.getEmail())).thenReturn(Optional.of(usuarioBase));
        when(passwordEncoder.matches("password123", "encodedPass")).thenReturn(true);

        Usuario login = usuarioService.login("juan@example.com", "password123");

        assertThat(login).isEqualTo(usuarioBase);
    }

    @Test
    void login_emailOContrasenaVacia() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> usuarioService.login("", "password123"));
        assertThat(ex.getMessage()).isEqualTo("Debe ingresar email y contraseña.");
    }

    @Test
    void login_emailNoEncontrado() {
        when(usuarioRepository.findByEmail("noexiste@example.com")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> usuarioService.login("noexiste@example.com", "password123"));
        assertThat(ex.getMessage()).isEqualTo("Email no encontrado.");
    }

    @Test
    void login_passwordIncorrecta() {
        usuarioBase.setPassword("encodedPass");
        when(usuarioRepository.findByEmail(usuarioBase.getEmail())).thenReturn(Optional.of(usuarioBase));
        when(passwordEncoder.matches("wrongpass", "encodedPass")).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> usuarioService.login("juan@example.com", "wrongpass"));
        assertThat(ex.getMessage()).isEqualTo("Contraseña inválida.");
    }


    @Test
    void actualizarDatos_ok() {
        Usuario nuevosDatos = new Usuario();
        nuevosDatos.setNombre("Juanito");
        nuevosDatos.setEmail("juanito@example.com");
        nuevosDatos.setPassword("nuevapass");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioBase));
        when(usuarioRepository.findByEmail("juanito@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("nuevapass")).thenReturn("encodedNuevaPass");
        when(usuarioRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Usuario actualizado = usuarioService.actualizarDatos(1L, nuevosDatos);

        assertThat(actualizado.getNombre()).isEqualTo("Juanito");
        assertThat(actualizado.getEmail()).isEqualTo("juanito@example.com");
        assertThat(actualizado.getPassword()).isEqualTo("encodedNuevaPass");
    }

    @Test
    void actualizarDatos_emailYaExiste() {
        Usuario nuevosDatos = new Usuario();
        nuevosDatos.setEmail("otro@example.com");

        Usuario otroUsuario = new Usuario();
        otroUsuario.setId(2L);
        otroUsuario.setEmail("otro@example.com");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioBase));
        when(usuarioRepository.findByEmail("otro@example.com")).thenReturn(Optional.of(otroUsuario));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> usuarioService.actualizarDatos(1L, nuevosDatos));
        assertThat(ex.getMessage()).isEqualTo("El email ya existe.");
    }

    @Test
    void actualizarDatos_usuarioNoEncontrado() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> usuarioService.actualizarDatos(99L, new Usuario()));
        assertThat(ex.getMessage()).isEqualTo("Usuario no encontrado");
    }


    @Test
    void eliminarCuenta_ok() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioBase));
        doNothing().when(usuarioRepository).delete(usuarioBase);

        usuarioService.eliminarCuenta(1L);

        verify(usuarioRepository).delete(usuarioBase);
    }

    @Test
    void eliminarCuenta_noEncontrado() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> usuarioService.eliminarCuenta(99L));
        assertThat(ex.getMessage()).isEqualTo("Usuario no encontrado");
    }


    @Test
    void buscarPorId_ok() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioBase));

        Usuario usuario = usuarioService.buscarPorId(1L);

        assertThat(usuario).isEqualTo(usuarioBase);
    }

    @Test
    void buscarPorId_noEncontrado() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> usuarioService.buscarPorId(99L));
        assertThat(ex.getMessage()).isEqualTo("Usuario no encontrado con ID: 99");
    }


    @Test
    void listarUsuarios_ok() {
        when(usuarioRepository.findAll()).thenReturn(List.of(usuarioBase));

        List<Usuario> usuarios = usuarioService.listarUsuarios();

        assertThat(usuarios).hasSize(1);
        assertThat(usuarios.get(0)).isEqualTo(usuarioBase);
    }


    @Test
    void listarPorRol_ok() {
        Usuario tecnico = new Usuario();
        tecnico.setRol(Rol.TECNICO);

        Usuario cliente = new Usuario();
        cliente.setRol(Rol.CLIENTE);

        when(usuarioRepository.findAll()).thenReturn(List.of(usuarioBase, tecnico, cliente));

        List<Usuario> clientes = usuarioService.listarPorRol(Rol.CLIENTE);

        assertThat(clientes).hasSize(1);
        assertThat(clientes.get(0).getRol()).isEqualTo(Rol.CLIENTE);
    }
}
