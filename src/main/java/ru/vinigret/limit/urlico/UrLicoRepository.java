package ru.vinigret.limit.urlico;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface UrLicoRepository<T extends UrLico> extends JpaRepository<T, Integer> {

    T getById(Integer id);

    Page<T> findAll(Pageable pageable);

    List<T> findAll();

    @Query("select max(z.porNum) from #{#entityName} z")
    Integer getLastPorNum();

}
