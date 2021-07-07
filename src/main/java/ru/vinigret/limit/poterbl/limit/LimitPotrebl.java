package ru.vinigret.limit.poterbl.limit;

import ru.vinigret.limit.poterbl.Potrebl;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("1")
public class LimitPotrebl extends Potrebl {

    public LimitPotrebl() {
        super();
    }
}
