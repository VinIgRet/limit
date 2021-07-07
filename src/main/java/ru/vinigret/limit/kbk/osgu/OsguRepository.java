package ru.vinigret.limit.kbk.osgu;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.vinigret.limit.kbk.osgu.Osgu;

import java.util.Optional;

@Repository
public interface OsguRepository extends JpaRepository<Osgu, Integer> {

    //@Query("select f from Osgu f where f.kod = :s")
    //Optional<Osgu> findByKod(@Param("s") String kod);

}
