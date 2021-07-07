package ru.vinigret.limit.kbk.osgu;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OsguDTO  {

    private Integer id = null;

    private String kod = "";

    private String name = "";

    //private LocalDate dateStart;

    //private LocalDate  dateEnd;

}
