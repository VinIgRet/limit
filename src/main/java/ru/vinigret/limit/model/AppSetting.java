package ru.vinigret.limit.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.vinigret.limit.urlico.UrLico;

import javax.persistence.*;

@Entity
//@Table(name = "appusers",  uniqueConstraints = {@UniqueConstraint(name = "APP_USER_UK", columnNames = "login") })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Column(name = "id", nullable = false)
    private Integer id;

    //@Column(name = "startyear", nullable = false)
    private Integer startYear = 2019;

    //@Column(name = "tekyear", nullable = false)
    private Integer tekYear = 2021;

    //@Column(name = "tekmonth", nullable = false)
    private Integer tekMonth = 1;

    //@Column(name = "centruchr", nullable = false)
    @OneToOne
    private UrLico centrUchr = null;
}
