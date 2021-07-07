package ru.vinigret.limit.poterbl.fakt;

import ru.vinigret.limit.poterbl.Potrebl;
import javax.persistence.*;

@Entity
@DiscriminatorValue("2")
public class FaktPotrebl extends Potrebl {


    public FaktPotrebl() {
        super();
    }
}
