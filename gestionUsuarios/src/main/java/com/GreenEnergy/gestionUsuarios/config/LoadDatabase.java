package com.GreenEnergy.gestionUsuarios.config;

import java.util.List;
import java.util.Locale;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.GreenEnergy.gestionUsuarios.model.Especialidad;
import com.GreenEnergy.gestionUsuarios.model.Rol;
import com.GreenEnergy.gestionUsuarios.model.Usuario;
import com.GreenEnergy.gestionUsuarios.repository.UsuarioRepository;

@Configuration
public class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        return args -> {

            if (usuarioRepository.findByEmail("admin@greenenergy.cl").isEmpty()) {
                Usuario admin = new Usuario();
                admin.setNombre("Admin");
                admin.setApellido("Principal");
                admin.setRut("11111111-1");
                admin.setEmail("admin@greenenergy.cl");
                admin.setTelefono("+56911111111");
                admin.setRol(Rol.ADMINISTRADOR);
                admin.setPassword(passwordEncoder.encode("admin123"));

                usuarioRepository.save(admin);
                System.out.println("Usuario admin creado");
            } else {
                System.out.println("Admin ya existe");
            }

            for (int i = 1; i <= 3; i++) {
                String email = "soporte" + i + "@greenenergy.cl";
                if (usuarioRepository.findByEmail(email).isEmpty()) {
                    Usuario soporte = new Usuario();
                    soporte.setNombre("TecnicoSoporte" + i);
                    soporte.setApellido("Apoyo");
                    soporte.setRut("2222222" + i + "-K");
                    soporte.setEmail(email);
                    soporte.setTelefono("+5692222222" + i);
                    soporte.setRol(Rol.TECNICO_SOPORTE);
                    soporte.setPassword(passwordEncoder.encode("soporte123"));

                    usuarioRepository.save(soporte);
                }
            }

            List<String> especialidades = List.of(
                    "electricista",
                    "instalador fotovoltaico",
                    "instalador de estructura",
                    "ayudante tecnico",
                    "limpieza de paneles");

            for (int s = 0; s < especialidades.size(); s++) {
                String especialidad = especialidades.get(s);
                for (int i = 1; i <= 5; i++) {
                    String email = especialidad.replace(" ", "") + i + "@greenenergy.cl";
                    if (usuarioRepository.findByEmail(email).isEmpty()) {
                        Usuario tecnico = new Usuario();
                        tecnico.setNombre(capitalize(especialidad) + i);
                        tecnico.setApellido("Especialista");
                        tecnico.setRut("3" + s + i + "33333-K");
                        tecnico.setEmail(email);
                        tecnico.setTelefono("+5693333333" + i);
                        tecnico.setRol(Rol.TECNICO);

                        String especialidadEnumStr = especialidad.toUpperCase().replace(" ", "_");
                        tecnico.setEspecialidad(Especialidad.valueOf(especialidadEnumStr));

                        tecnico.setPassword(passwordEncoder.encode("tecnico123"));

                        usuarioRepository.save(tecnico);
                    }
                }
            }

            System.out.println("Técnicos de soporte y técnicos especializados cargados.");

            System.out.println("Cargando clientes");
            for (int i = 1; i <= 5; i++) {
                String email = "cliente" + i + "@greenenergy.cl";
                if (usuarioRepository.findByEmail(email).isEmpty()) {
                    Usuario cliente = new Usuario();
                    cliente.setNombre("Cliente" + i);
                    cliente.setApellido("Solar");
                    cliente.setRut("4444444" + i + "-K");
                    cliente.setEmail(email);
                    cliente.setTelefono("+5694444444" + i);
                    cliente.setRol(Rol.CLIENTE);
                    cliente.setPassword(passwordEncoder.encode("cliente123"));
                    cliente.setEspecialidad(null);

                    try {
                        usuarioRepository.save(cliente);
                        System.out.println("Cliente creado: " + email);
                    } catch (Exception e) {
                        System.err.println("Error al guardar cliente " + email + ": " + e.getMessage());
                    }
                } else {
                    System.out.println("Cliente ya existe: " + email);
                }
            }

        };
    }

    private String capitalize(String texto) {
        if (texto == null || texto.isEmpty())
            return texto;
        return texto.substring(0, 1).toUpperCase(Locale.ROOT) + texto.substring(1).toLowerCase(Locale.ROOT);
    }

}
