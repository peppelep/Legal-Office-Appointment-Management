package piattaforme.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import piattaforme.demo.exceptions.NoAppuntamentoExistInRepository;
import piattaforme.demo.models.Appuntamento;
import piattaforme.demo.repository.AppuntamentoRepository;

import java.util.List;
import java.util.Optional;


@Service
public class AppuntamentoServiceImpl implements AppuntamentoService {

    @Autowired
    private AppuntamentoRepository appuntamentoRepository;

    @Transactional(readOnly = true)
    @Override
    public List<Appuntamento> getAll() throws NoAppuntamentoExistInRepository {
        List<Appuntamento> allAppuntamenti = appuntamentoRepository.findAll();

        if (allAppuntamenti.isEmpty()) {
            throw new NoAppuntamentoExistInRepository();
        } else {
            return allAppuntamenti;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Appuntamento getById(Long id) throws NoAppuntamentoExistInRepository {
        Optional<Appuntamento> appuntamento = appuntamentoRepository.findById(id);
        if (appuntamento.isEmpty()) {
            throw new NoAppuntamentoExistInRepository();
        } else {
            Appuntamento app = appuntamento.get();
            return app ; 
        }
    }

    @Transactional
    @Override
    public Appuntamento add1(Appuntamento appuntamento) {
        return appuntamentoRepository.save(appuntamento);
    }

    @Transactional
    @Override
    public void remove(Appuntamento appuntamento) {
        appuntamentoRepository.delete(appuntamento);
    }

    @Override
    public Appuntamento save(Appuntamento appuntamento) {
        return appuntamentoRepository.save(appuntamento);
    }
}