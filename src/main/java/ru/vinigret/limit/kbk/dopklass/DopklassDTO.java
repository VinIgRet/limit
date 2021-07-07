package ru.vinigret.limit.kbk.dopklass;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DopklassDTO {

    private Integer id;

    private String kod;

    private String name;

    private Timestamp dateStart;

    private Timestamp dateEnd;

    private Integer osgu;

}
