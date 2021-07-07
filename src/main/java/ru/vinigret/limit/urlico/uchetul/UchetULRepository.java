package ru.vinigret.limit.urlico.uchetul;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.vinigret.limit.other.IdNameInterface;
import ru.vinigret.limit.urlico.UrLicoRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public interface UchetULRepository extends UrLicoRepository<UchetUL> {

    /*
    List<UchetUL> findAllByKodStartingWith(String kod);

    List<UchetUL> findAllByUchredit(UchetUL uchredit);

    List<UchetUL> findAllByParent(UchetUL parent);

    List<UchetUL> findAllByParentAndUchredit(UchetUL parent, UchetUL uchredit);

    Long countAllByParentEquals(Integer parentId);

    UchetUL findFirstByParentEqualsOrderByKodDesc(UchetUL parent);
    */

    @Query("SELECT max(z.porNum) FROM UchetUL z where z.groupUchr = false")
    Integer lastPorNumUchr();

    @Query("SELECT max(z.porNum) FROM UchetUL z where z.groupUchr = true and z.level = :lvl and z.uchreditel = :uchredit")
    Integer lastPorNumGroupFromLevelAndUchredit(@Param("lvl") Integer level, @Param("uchredit") UchetUL uchredit);

    @Query("SELECT max(z.porNum) FROM UchetUL z where z.groupUchr = true and z.level = :lvl")
    Integer lastPorNumGroupFromLevel(@Param("lvl") Integer level);

    List<IdNameInterface> findAllByGroupUchrIsFalse();

    List<IdNameInterface> findAllByUchreditel(UchetUL uchredit);

    List<IdNameInterface> findAllByGrbsIsTrue();

    List<IdNameInterface> findAllByGroupUchrIsTrue();

    List<IdNameInterface> findAllByUchreditelAndGroupUchrIsFalse(UchetUL uchredit);

    List<IdNameInterface> findAllByParentAndGroupUchrIsFalse(UchetUL parent);

    List<IdNameInterface> findAllByGrbsIsTrueOrGrbsIsFalse();

    Optional<UchetUL> findByUchreditelEqualsAndLevelEqualsAndUchetULKind(UchetUL uchred, Integer lvl, UchetULKind kind);

}
