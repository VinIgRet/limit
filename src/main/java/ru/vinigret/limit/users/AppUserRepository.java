package ru.vinigret.limit.users;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.vinigret.limit.other.IdNameInterface;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Integer> {

    AppUser getById(Integer id);

    AppUser findByLogin(String s);

    Page<AppUser> findAll(Pageable page);

    @Query("select u from AppUser u where login <> :login")
    Page<AppUser> findAllByLoginNotEquals(@Param("login") String login, Pageable page);

    Boolean existsByEmailEquals(String email);

    Boolean existsByLoginEquals(String login);

    AppUser getByLoginAndId(String login, Integer id);

    Optional<AppUser> getByLoginEquals(String login);

    void deleteById(Integer id);

    //@Modifying
    //@Query("update AppUser f set f.deleted = 1 where f.id = :id")
    //void deleteSafeById(@Param("id") Integer id);
}
