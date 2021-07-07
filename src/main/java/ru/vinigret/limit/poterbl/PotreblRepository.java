package ru.vinigret.limit.poterbl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;


@NoRepositoryBean
public interface PotreblRepository<T extends Potrebl> extends JpaRepository<T, Integer> {

    T getById(Integer id);

    Page<T> findAll(Pageable pageable);

    List<T> findByIerKodUchr(String ierKod);

}
