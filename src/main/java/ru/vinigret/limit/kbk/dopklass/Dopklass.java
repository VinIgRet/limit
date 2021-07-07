package ru.vinigret.limit.kbk.dopklass;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.vinigret.limit.kbk.osgu.Osgu;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dopklass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String kod;

    private String name;

    private java.sql.Timestamp dateStart;

    private java.sql.Timestamp dateEnd;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="osgu")
    private Osgu osgu;
}
