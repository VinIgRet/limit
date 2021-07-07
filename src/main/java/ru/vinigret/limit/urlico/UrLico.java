package ru.vinigret.limit.urlico;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
//@Table(uniqueConstraints = {@UniqueConstraint(name = "UnicInn", columnNames = "inn")})
@Inheritance(strategy = InheritanceType.JOINED)
public class UrLico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String kod;

    private String inn;

    private String name;

    private Integer porNum;

    @Override
    public String toString() {
        return "UrLico{" +
                "id=" + id +
                ", inn='" + inn + '\'' +
                ", name='" + name + '\'' +
                ", porNum=" + porNum +
                '}';
    }
}
