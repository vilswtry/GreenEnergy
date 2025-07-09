package com.GreenEnergy.gestionUsuarios.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import com.GreenEnergy.gestionUsuarios.model.Especialidad;
import com.GreenEnergy.gestionUsuarios.model.Rol;
import com.GreenEnergy.gestionUsuarios.model.Usuario;
import com.GreenEnergy.gestionUsuarios.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(UsuarioController.class)
public class UsuarioControllerTest {

        @MockBean
        private UsuarioService usuarioService;

        @Autowired
        private MockMvc mockMvc;

        @Test
        @WithMockUser(roles = { "ADMINISTRADOR" })
        void registrarCliente_deberiaRetornarCreatedYUsuario() throws Exception {
                Usuario cliente = new Usuario();
                cliente.setId(1L);
                cliente.setNombre("Juan");
                cliente.setApellido("Perez");
                cliente.setRut("12345678-9");
                cliente.setEmail("juan@example.com");
                cliente.setTelefono("+56912345678");
                cliente.setPassword("encodedpass");
                cliente.setRol(Rol.CLIENTE);
                cliente.setEspecialidad(null);

                when(usuarioService.registrarCliente(org.mockito.ArgumentMatchers.any(Usuario.class)))
                                .thenReturn(cliente);

                String clienteJson = """
                                {
                                  "nombre": "Juan",
                                  "apellido": "Perez",
                                  "rut": "12345678-9",
                                  "email": "juan@example.com",
                                  "telefono": "+56912345678",
                                  "password": "contraseña123",
                                  "rol": "CLIENTE"
                                }
                                """;

                mockMvc.perform(post("/api/usuarios/registro/cliente")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(clienteJson))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id").value(1L))
                                .andExpect(jsonPath("$.email").value("juan@example.com"))
                                .andExpect(jsonPath("$.rol").value("CLIENTE"))
                                .andExpect(jsonPath("$.especialidad").doesNotExist());
        }

        @Test
        @WithMockUser(roles = { "ADMINISTRADOR" })
        void registrarCliente_conErrorDeValidacion_deberiaRetornarBadRequest() throws Exception {
                when(usuarioService.registrarCliente(org.mockito.ArgumentMatchers.any(Usuario.class)))
                                .thenThrow(new IllegalArgumentException("El email ya está registrado."));

                String clienteJson = """
                                {
                                  "nombre": "Juan",
                                  "apellido": "Perez",
                                  "rut": "12345678-9",
                                  "email": "juan@example.com",
                                  "telefono": "+56912345678",
                                  "password": "contraseña123"
                                }
                                """;

                mockMvc.perform(post("/api/usuarios/registro/cliente")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(clienteJson))
                                .andExpect(status().isBadRequest())
                                .andExpect(content().string("El email ya está registrado."));
        }

        @Test
        @WithMockUser(roles = { "ADMINISTRADOR" })
        void registrarTecnico_deberiaRetornarCreatedYUsuario() throws Exception {
                Usuario tecnico = new Usuario();
                tecnico.setId(2L);
                tecnico.setNombre("Ana");
                tecnico.setApellido("Gomez");
                tecnico.setRut("98765432-1");
                tecnico.setEmail("ana@example.com");
                tecnico.setTelefono("+56987654321");
                tecnico.setPassword("encodedpass");
                tecnico.setRol(Rol.TECNICO);
                tecnico.setEspecialidad(Especialidad.ELECTRICISTA);

                when(usuarioService.registrarTecnico(org.mockito.ArgumentMatchers.any(Usuario.class)))
                                .thenReturn(tecnico);

                String tecnicoJson = """
                                {
                                  "nombre": "Ana",
                                  "apellido": "Gomez",
                                  "rut": "98765432-1",
                                  "email": "ana@example.com",
                                  "telefono": "+56987654321",
                                  "password": "contraseña123",
                                  "rol": "TECNICO",
                                  "especialidad": "ELECTRICISTA"
                                }
                                """;

                mockMvc.perform(post("/api/usuarios/registro/tecnico")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(tecnicoJson))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id").value(2L))
                                .andExpect(jsonPath("$.email").value("ana@example.com"))
                                .andExpect(jsonPath("$.rol").value("TECNICO"))
                                .andExpect(jsonPath("$.especialidad").value("ELECTRICISTA"));
        }

        @Test
        @WithMockUser(roles = { "ADMINISTRADOR" })
        void registrarAdministrador_deberiaRetornarCreatedYUsuario() throws Exception {
                Usuario admin = new Usuario();
                admin.setId(3L);
                admin.setNombre("Admin");
                admin.setApellido("Principal");
                admin.setRut("11111111-1");
                admin.setEmail("admin@example.com");
                admin.setTelefono("+56911111111");
                admin.setPassword("encodedpass");
                admin.setRol(Rol.ADMINISTRADOR);

                when(usuarioService.registrarAdministrador(org.mockito.ArgumentMatchers.any(Usuario.class)))
                                .thenReturn(admin);

                String adminJson = """
                                {
                                  "nombre": "Admin",
                                  "apellido": "Principal",
                                  "rut": "11111111-1",
                                  "email": "admin@example.com",
                                  "telefono": "+56911111111",
                                  "password": "admin123",
                                  "rol": "ADMINISTRADOR"
                                }
                                """;

                mockMvc.perform(post("/api/usuarios/registro/administrador")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(adminJson))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id").value(3L))
                                .andExpect(jsonPath("$.email").value("admin@example.com"))
                                .andExpect(jsonPath("$.rol").value("ADMINISTRADOR"));
        }

        @Test
        @WithMockUser(roles = { "ADMINISTRADOR" })
        void registrarTecnicoSoporte_deberiaRetornarCreatedYUsuario() throws Exception {
                Usuario tecnicoSoporte = new Usuario();
                tecnicoSoporte.setId(4L);
                tecnicoSoporte.setNombre("Sofia");
                tecnicoSoporte.setApellido("Soporte");
                tecnicoSoporte.setRut("55555555-5");
                tecnicoSoporte.setEmail("soporte@example.com");
                tecnicoSoporte.setTelefono("+56955555555");
                tecnicoSoporte.setPassword("encodedpass");
                tecnicoSoporte.setRol(Rol.TECNICO_SOPORTE);

                when(usuarioService.registrarTecnicoSoporte(org.mockito.ArgumentMatchers.any(Usuario.class)))
                                .thenReturn(tecnicoSoporte);

                String tecnicoSoporteJson = """
                                {
                                  "nombre": "Sofia",
                                  "apellido": "Soporte",
                                  "rut": "55555555-5",
                                  "email": "soporte@example.com",
                                  "telefono": "+56955555555",
                                  "password": "soporte123",
                                  "rol": "TECNICO_SOPORTE"
                                }
                                """;

                mockMvc.perform(post("/api/usuarios/registro/tecnico-soporte")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(tecnicoSoporteJson))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id").value(4L))
                                .andExpect(jsonPath("$.email").value("soporte@example.com"))
                                .andExpect(jsonPath("$.rol").value("TECNICO_SOPORTE"));
        }

        @Test
        @WithMockUser
        void login_deberiaRetornarOKYUsuario() throws Exception {
                Usuario usuario = new Usuario();
                usuario.setId(5L);
                usuario.setNombre("Juan");
                usuario.setApellido("Perez");
                usuario.setRut("12345678-9");
                usuario.setEmail("juan@example.com");
                usuario.setTelefono("+56912345678");
                usuario.setPassword("encodedpass");
                usuario.setRol(Rol.CLIENTE);

                when(usuarioService.login("juan@example.com", "password123")).thenReturn(usuario);

                String loginJson = """
                                {
                                  "email": "juan@example.com",
                                  "password": "password123"
                                }
                                """;

                mockMvc.perform(post("/api/usuarios/login")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(loginJson))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.email").value("juan@example.com"))
                                .andExpect(jsonPath("$.rol").value("CLIENTE"));
        }

        // login errores
        @Test
        @WithMockUser
        void login_conEmailVacio_deberiaRetornarBadRequest() throws Exception {
                Map<String, String> loginData = new HashMap<>();
                loginData.put("email", "");
                loginData.put("password", "password123");

                String json = new ObjectMapper().writeValueAsString(loginData);

                when(usuarioService.login("", "password123"))
                                .thenThrow(new IllegalArgumentException("Debe ingresar email y contraseña."));

                mockMvc.perform(post("/api/usuarios/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                                .with(csrf()))
                                .andExpect(status().isBadRequest())
                                .andExpect(content().string("Debe ingresar email y contraseña."));
        }

        @Test
        @WithMockUser
        void login_conCredencialesInvalidas_deberiaRetornarUnauthorized() throws Exception {
                Map<String, String> loginData = new HashMap<>();
                loginData.put("email", "juan@example.com");
                loginData.put("password", "wrongpass");

                String json = new ObjectMapper().writeValueAsString(loginData);

                when(usuarioService.login("juan@example.com", "wrongpass"))
                                .thenThrow(new RuntimeException("Contraseña inválida."));

                mockMvc.perform(post("/api/usuarios/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                                .with(csrf()))
                                .andExpect(status().isUnauthorized())
                                .andExpect(content().string("Contraseña inválida."));
        }

        @Test
        @WithMockUser(roles = { "ADMINISTRADOR" })
        void actualizarDatos_deberiaRetornarOKYUsuarioActualizado() throws Exception {
                Usuario actualizado = new Usuario();
                actualizado.setId(1L);
                actualizado.setNombre("Juan");
                actualizado.setApellido("Perez");
                actualizado.setRut("12345678-9");
                actualizado.setEmail("juan@example.com");
                actualizado.setTelefono("+56912345678");
                actualizado.setPassword("encodedpass");
                actualizado.setRol(Rol.CLIENTE);

                String jsonActualizado = """
                                {
                                  "nombre": "Juan",
                                  "apellido": "Perez",
                                  "email": "juan@example.com"
                                }
                                """;

                when(usuarioService.actualizarDatos(eq(1L), any(Usuario.class))).thenReturn(actualizado);

                mockMvc.perform(put("/api/usuarios/1")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonActualizado))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(1))
                                .andExpect(jsonPath("$.email").value("juan@example.com"));
        }

        // error actualizar datos
        @Test
        @WithMockUser(roles = { "ADMINISTRADOR" })
        void actualizarDatos_conIdNoExistente_deberiaRetornarNotFound() throws Exception {
                when(usuarioService.actualizarDatos(org.mockito.ArgumentMatchers.anyLong(),
                                org.mockito.ArgumentMatchers.any(Usuario.class)))
                                .thenThrow(new RuntimeException("Usuario no encontrado"));

                String usuarioJson = """
                                {
                                  "nombre": "Juan",
                                  "apellido": "Perez",
                                  "rut": "12345678-9",
                                  "email": "juan@example.com",
                                  "telefono": "+56912345678",
                                  "password": "contraseña123"
                                }
                                """;

                mockMvc.perform(put("/api/usuarios/99")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(usuarioJson))
                                .andExpect(status().isNotFound())
                                .andExpect(content().string("Usuario no encontrado"));
        }

        @Test
        @WithMockUser(roles = { "ADMINISTRADOR" })
        void eliminarCuenta_deberiaRetornarNoContent() throws Exception {
                doNothing().when(usuarioService).eliminarCuenta(1L);

                mockMvc.perform(delete("/api/usuarios/1")
                                .with(csrf()))
                                .andExpect(status().isNoContent());
        }

        // error eliminar
        @Test
        @WithMockUser(roles = { "ADMINISTRADOR" })
        void eliminarCuenta_conIdNoExistente_deberiaRetornarNotFound() throws Exception {
                doThrow(new RuntimeException("Usuario no encontrado")).when(usuarioService).eliminarCuenta(99L);

                mockMvc.perform(delete("/api/usuarios/99")
                                .with(csrf()))
                                .andExpect(status().isNotFound())
                                .andExpect(content().string("Usuario no encontrado"));
        }

        @Test
        @WithMockUser(roles = { "ADMINISTRADOR" })
        void listarUsuarios_deberiaRetornarListaUsuariosYStatusOK() throws Exception {
                List<Usuario> usuarios = Arrays.asList(
                                new Usuario(1L, "Juan", "Perez", "12345678-9", "juan@example.com", "+56912345678",
                                                "pass", Rol.CLIENTE, null),
                                new Usuario(2L, "Ana", "Gomez", "98765432-1", "ana@example.com", "+56987654321", "pass",
                                                Rol.TECNICO, null));

                when(usuarioService.listarUsuarios()).thenReturn(usuarios);

                mockMvc.perform(get("/api/usuarios"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$").isArray())
                                .andExpect(jsonPath("$.length()").value(2))
                                .andExpect(jsonPath("$[0].email").value("juan@example.com"))
                                .andExpect(jsonPath("$[1].rol").value("TECNICO"));
        }

        @Test
        @WithMockUser(roles = { "ADMINISTRADOR" })
        void obtenerUsuarioPorId_deberiaRetornarUsuarioYStatusOK() throws Exception {
                Usuario usuario = new Usuario(1L, "Juan", "Perez", "12345678-9", "juan@example.com", "+56912345678",
                                "pass", Rol.CLIENTE, null);

                when(usuarioService.buscarPorId(1L)).thenReturn(usuario);

                mockMvc.perform(get("/api/usuarios/1"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.email").value("juan@example.com"))
                                .andExpect(jsonPath("$.rol").value("CLIENTE"));
        }

        @Test
        @WithMockUser(roles = { "ADMINISTRADOR" })
        void obtenerUsuarioPorId_notFound() throws Exception {
                when(usuarioService.buscarPorId(7L)).thenThrow(new RuntimeException("no encontrado"));

                mockMvc.perform(get("/api/usuarios/7"))
                                .andExpect(status().isNotFound());
        }

        @Test
        @WithMockUser(roles = { "ADMINISTRADOR" })
        void listarUsuariosPorRol_deberiaRetornarUsuariosFiltradosYStatusOK() throws Exception {
                List<Usuario> usuarios = Arrays.asList(
                                new Usuario(2L, "Ana", "Gomez", "98765432-1", "ana@example.com", "+56987654321", "pass",
                                                Rol.TECNICO, null));

                when(usuarioService.listarPorRol(Rol.TECNICO)).thenReturn(usuarios);

                mockMvc.perform(get("/api/usuarios/rol/TECNICO"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$").isArray())
                                .andExpect(jsonPath("$.length()").value(1))
                                .andExpect(jsonPath("$[0].rol").value("TECNICO"));
        }

}
