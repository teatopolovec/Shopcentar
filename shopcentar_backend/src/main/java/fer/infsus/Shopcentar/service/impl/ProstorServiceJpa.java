package fer.infsus.Shopcentar.service.impl;

import fer.infsus.Shopcentar.dao.EtazaRepository;
import fer.infsus.Shopcentar.dao.ProstorRepository;
import fer.infsus.Shopcentar.domain.*;
import fer.infsus.Shopcentar.dto.EtazaDTO;
import fer.infsus.Shopcentar.dto.KategorijaDTO;
import fer.infsus.Shopcentar.dto.TrgovinaDTO;
import fer.infsus.Shopcentar.service.ProstorService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

@Service
public class ProstorServiceJpa implements ProstorService {
    @Autowired
    private ProstorRepository prostorRepo;
    @Autowired
    private EtazaRepository etazaRepo;

    @Override
    public List<Map<String, Object>> dohvatiProstore(){
        List<Prostor> rezultati = prostorRepo.findAll();
        List<Map<String, Object>> prostori = new ArrayList<>();
        for (Prostor red : rezultati) {
            Map<String, Object> mapa = new HashMap<>();
            mapa.put("idProstora", red.getIdProstora());
            mapa.put("etaza", red.getEtaza().getBrojEtaze());
            mapa.put("kvadratura", red.getKvadratura());
            prostori.add(mapa);
        }
        return prostori;
    }

    @Override
    public List<Map<String, Object>> dohvatiEtaze() {
        List<Etaza> rezultati = etazaRepo.findAll();
        List<Map<String, Object>> etaze = new ArrayList<>();
        for (Etaza red : rezultati) {
            Map<String, Object> mapa = new HashMap<>();
            mapa.put("idEtaze", red.getIdEtaze());
            mapa.put("brojEtaze", red.getBrojEtaze());
            mapa.put("opis", red.getOpis());
            etaze.add(mapa);
        }
        return etaze;
    }

    @Transactional
    public Etaza kreirajEtazu(EtazaDTO etaza) throws IOException {
            Etaza e = new Etaza();
            System.out.print(e);
            if (etazaRepo.existsByBrojEtaze(etaza.getBrojEtaze())) {
                throw new IllegalArgumentException("Etaža s tim brojem već postoji.");
            }
            if (etaza.getBrojEtaze() == null) {
                throw new IllegalArgumentException("Etaža mora imati broj.");
            }
            e.setBrojEtaze(etaza.getBrojEtaze());

            if (etaza.getOpis() == null || etaza.getOpis().isEmpty()) {
                throw new IllegalArgumentException("Etaža mora imati opis.");
            }
            e.setOpis(etaza.getOpis());

            return etazaRepo.save(e);
        }


    @Override
    @Transactional
    public Etaza azurirajEtazu(Integer id, EtazaDTO etaza) {
        Etaza e = etazaRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Etaža s ID " + id + " nije pronađena."));

        boolean novo = false;
        if (!Objects.equals(etaza.getBrojEtaze(), e.getBrojEtaze())) {
            if (etazaRepo.existsByBrojEtaze(etaza.getBrojEtaze())) {
                throw new IllegalArgumentException("Etaža s tim brojem već postoji.");
            }
            if (etaza.getBrojEtaze() == null) {
                throw new IllegalArgumentException("Etaža mora imati broj.");
            }
            e.setBrojEtaze(etaza.getBrojEtaze());
            novo = true;
        }

        if (!Objects.equals(e.getOpis(), etaza.getOpis())) {
            if (etaza.getOpis() == null || etaza.getOpis().isEmpty()) {
                throw new IllegalArgumentException("Neispravan opis.");
            }
            e.setOpis(etaza.getOpis());
        }

        return etazaRepo.save(e);
    }

    @Override
    @Transactional
    public void izbrisiEtazu(Integer id) {
        Etaza e = etazaRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Etaža s ID " + id + " nije pronađena."));
        etazaRepo.delete(e);
    }

    @Override
    public Optional<EtazaDTO> findById(Integer id) {
        return etazaRepo.findById(id)
                .map(e -> {
                    EtazaDTO dto = new EtazaDTO();
                    dto.setBrojEtaze(e.getBrojEtaze());
                    dto.setOpis(e.getOpis());
                    System.out.print(dto);
                    return dto;});
    }

}


