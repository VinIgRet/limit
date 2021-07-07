package ru.vinigret.limit.usluga;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vinigret.limit.usluga.Usluga;

@Repository
public interface UslugaRepository extends JpaRepository<Usluga, Integer> {


}
