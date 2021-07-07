package ru.vinigret.limit.urlico.kontrul;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.vinigret.limit.urlico.UrLicoDTO;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public class KontrULDTO extends UrLicoDTO {

    @Getter
    @Setter
    String tel;

    @Getter
    @Setter
    List<Integer> uslugaList;

}
