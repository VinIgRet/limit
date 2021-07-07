package ru.vinigret.limit.usluga;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.vinigret.limit.kbk.dopklass.Dopklass;
import ru.vinigret.limit.kbk.osgu.Osgu;
import ru.vinigret.limit.pribor.PriborKind;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usluga {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @ManyToOne
    private Osgu osgu;

    @ManyToOne
    private Dopklass dopKlass;

    private PriborKind priborKind = PriborKind.RASCHET;

    //@ManyToMany
    //Set<Pribor> priborSet;

}
