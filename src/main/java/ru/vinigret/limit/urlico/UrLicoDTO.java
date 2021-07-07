package ru.vinigret.limit.urlico;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
public class UrLicoDTO {

    @Getter
    @Setter
    private Integer id;

    @Getter
    @Setter
    @Size(min = 10, max = 10, message = "длина ИНН юридического лица 10 знаков")
    private String inn;

    @Getter
    @Setter
    @NotNull
    private String name;

    @Getter
    @Setter
    private String action = ".new";

    @Getter
    @Setter
    protected String returnhttp = "";

    public UrLicoDTO(UrLico entity) {
        this.id = entity.getId();
        this.inn = entity.getInn();
        this.name = entity.getName();
    }
}
