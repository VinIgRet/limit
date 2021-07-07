package ru.vinigret.limit.poterbl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PotreblDTO {
    private Integer id;

    //@NumberFormat(style= NumberFormat.Style.CURRENCY)
    private BigDecimal potrebVolume;

    //@NumberFormat(style= NumberFormat.Style.CURRENCY)
    private BigDecimal potrebSumma;

    private Integer potrebYear;

    private Integer potrebMonth;

    private LocalDate dateCreation;

    private Integer dogovor;

    private Integer usluga;

    private Integer obesp;

    private Integer uchetUL;

    private Integer kontrUL;

    private Integer docOsn;

    private String ierKodUchr = "";

}
