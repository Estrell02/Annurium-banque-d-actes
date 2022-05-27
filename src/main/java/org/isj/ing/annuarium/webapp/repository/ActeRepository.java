package org.isj.ing.annuarium.webapp.repository;

import org.isj.ing.annuarium.webapp.model.entities.Acte;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ActeRepository extends JpaRepository<Acte, Long> {

    Optional<Acte> findActeByNumero(String numero);

    Optional<List<Acte>>findActeByNumeroOrNom(String motcle1, String motcle2);

    boolean existsByNumeroIgnoreCase(String numero);

}
