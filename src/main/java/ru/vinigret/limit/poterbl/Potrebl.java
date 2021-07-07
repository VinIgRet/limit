package ru.vinigret.limit.poterbl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.vinigret.limit.docum.docosn.DocOsn;
import ru.vinigret.limit.docum.dogovor.Dogovor;
import ru.vinigret.limit.kbk.obesp.Obesp;
import ru.vinigret.limit.usluga.Usluga;
import ru.vinigret.limit.urlico.kontrul.KontrUL;
import ru.vinigret.limit.urlico.uchetul.UchetUL;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="typ", discriminatorType = DiscriminatorType.INTEGER)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Potrebl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //@NumberFormat(style= NumberFormat.Style.CURRENCY)
    private BigDecimal potrebVolume;

    //@NumberFormat(style= NumberFormat.Style.CURRENCY)
    private BigDecimal potrebSumma;

    private Integer potrebYear;

    private Integer potrebMonth;

    private java.sql.Timestamp dateCreation;

    @ManyToOne
    private Dogovor dogovor;

    @ManyToOne
    private Usluga usluga;

    @ManyToOne
    private Obesp obesp;

    @ManyToOne
    private UchetUL uchetUL;

    @ManyToOne
    private KontrUL kontrUL;

    @ManyToOne
    private DocOsn docOsn;

    private String ierKodUchr = "";
}
