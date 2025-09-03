package hr.e2systems.repository;

import hr.e2systems.entity.Metar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface MetarRepository extends JpaRepository<Metar, Long> {
    Optional<Metar> findTopByIcaoCodeOrderByReceivedAtDesc(String icaoCode);
}
