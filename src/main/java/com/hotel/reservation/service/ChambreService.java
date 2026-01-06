package com.hotel.reservation.service;

import com.hotel.reservation.entity.Chambre;
import com.hotel.reservation.repository.ChambreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChambreService {
    private final ChambreRepository chambreRepository;

    public Chambre createChambre(Chambre chambre) {
        return chambreRepository.save(chambre);
    }

    public Optional<Chambre> getChambre(Long id) {
        return chambreRepository.findById(id);
    }

    public List<Chambre> getAllChambres() {
        return chambreRepository.findAll();
    }
}

