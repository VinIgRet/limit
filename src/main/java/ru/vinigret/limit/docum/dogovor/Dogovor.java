package ru.vinigret.limit.docum.dogovor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.vinigret.limit.urlico.kontrul.KontrUL;
import ru.vinigret.limit.urlico.uchetul.UchetUL;
import ru.vinigret.limit.usluga.Usluga;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dogovor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String kod;

    private String name;

    private java.sql.Timestamp dateCreation;

    private java.sql.Timestamp dateStart;

    private java.sql.Timestamp dateEnd;

    @OneToOne
    private UchetUL uchetUL;

    @OneToOne
    private KontrUL kontrUL;

    @ManyToOne
    private Usluga usluga;

    // @OneToMany
    //Set<Obesp> obespSet;

}
