package com.GreenEnergy.atencionClientes.service;

import com.GreenEnergy.atencionClientes.model.Solicitud;
import com.GreenEnergy.atencionClientes.repository.SolicitudRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class SolicitudService {

    @Autowired
    private SolicitudRepository repo;

    public Solicitud crear(Solicitud s) {
        return repo.save(s);
    }

    public List<Solicitud> obtenerTodas() {
        return repo.findAll();
    }
}
