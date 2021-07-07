package ru.vinigret.limit.urlico.kontrul;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.vinigret.limit.usluga.Usluga;
import ru.vinigret.limit.urlico.UrLico;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KontrUL extends UrLico {

    String tel = "";

    @ManyToMany
    List<Usluga> uslugaList;

    //public KontrUL() {
    //    super();
    //}

}
