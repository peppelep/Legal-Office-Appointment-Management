package piattaforme.demo.services;

import piattaforme.demo.models.Appuntamento;
import piattaforme.demo.exceptions.NoAppuntamentoExistInRepository;
import java.util.List;

public interface AppuntamentoService {
    List<Appuntamento> getAll() throws NoAppuntamentoExistInRepository;

    Appuntamento getById(Long id) throws NoAppuntamentoExistInRepository;

    Appuntamento add1(Appuntamento appuntamento);

    void remove(Appuntamento appuntamento);

    Appuntamento save(Appuntamento appuntamento);
}
