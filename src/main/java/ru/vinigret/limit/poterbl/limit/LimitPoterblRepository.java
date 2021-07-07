package ru.vinigret.limit.poterbl.limit;

import org.springframework.stereotype.Repository;
import ru.vinigret.limit.poterbl.PotreblRepository;
import ru.vinigret.limit.urlico.UrLicoRepository;
import ru.vinigret.limit.urlico.uchetul.UchetUL;

import java.util.List;

@Repository
public interface LimitPoterblRepository extends PotreblRepository<LimitPotrebl> {

}
