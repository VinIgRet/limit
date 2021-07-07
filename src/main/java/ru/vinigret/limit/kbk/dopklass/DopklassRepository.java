package ru.vinigret.limit.kbk.dopklass;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.vinigret.limit.kbk.dopklass.Dopklass;
import ru.vinigret.limit.kbk.osgu.Osgu;

@Repository
public interface DopklassRepository extends JpaRepository<Dopklass, Integer> {

    //@Query("select f from Dopklass f where f.kod = :s")
    //Dopklass findOneByIdKod(@Param("s") String kod);

}
