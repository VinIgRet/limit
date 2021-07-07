package ru.vinigret.limit.pokazania;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.vinigret.limit.pribor.Pribor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pokasania {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne//(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName="id")
    private Pribor pribor;

    private Timestamp dateCreation;

    private Integer pokYear;

    private Integer pokMonth;

    private BigDecimal tekValue;
}
