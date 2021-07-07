package ru.vinigret.limit.urlico.uchetul;

import lombok.Getter;
import lombok.Setter;
import ru.vinigret.limit.urlico.UrLico;
import ru.vinigret.limit.urlico.UrLicoDTO;
import ru.vinigret.limit.users.AppUser;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

public class UchetULDTO extends UrLicoDTO {

    @Getter
    @Setter
    private Integer uchetULKind = 0;

    @Getter
    @Setter
    private Integer parent = 0;

    @Getter
    @Setter
    private boolean groupUchr = false;

    @Getter
    @Setter
    private Integer uchreditel = 0;

    @Getter
    @Setter
    @Size(min = 2, max = 4, message = "2 или 4 знака")
    private String razdelBK = "";

    @Getter
    @Setter
    @Size(min = 2, max = 4, message = "2 или 4 знака")
    private String podrazdelBK = "";

    public UchetULDTO() {
        super();
    }

}
