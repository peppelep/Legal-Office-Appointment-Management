package piattaforme.demo.repository;

import piattaforme.demo.models.Prenotazione;
import piattaforme.demo.models.StatoPrenotazione;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrenotazioneRepository extends JpaRepository<Prenotazione, Long> {
    List<Prenotazione> findAllByOrderByIdAsc();       // Metodo per trovare tutte le prenotazioni ordinate per id
    List<Prenotazione> findByUsernameCliente(String username);
    List<Prenotazione> findByAppuntamento(Long id);
    List<Prenotazione> findByAppuntamentoAndStato(Long appuntamento, StatoPrenotazione stato);

}
