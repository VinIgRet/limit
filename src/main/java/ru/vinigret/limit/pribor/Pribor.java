package ru.vinigret.limit.pribor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.vinigret.limit.pribor.PriborKind;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pribor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String kod;

    private PriborKind priborKind = PriborKind.RASCHET;

    private java.sql.Timestamp dateStart;

    private java.sql.Timestamp dateEnd;

}
