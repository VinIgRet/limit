package ru.vinigret.limit.urlico.uchetul;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class ListDTO{

    //номер страницы
    @Getter
    @Setter
    private Integer p;

    //количество на страницу
    @Getter
    @Setter
    private Integer n;

    //поле сортировки
    @Getter
    @Setter
    private String s;

    //направление сортировки
    @Getter
    @Setter
    private String l;

    //действие
    @Getter
    @Setter
    private String a;

    //записи (Id) для действия
    @Getter
    @Setter
    private List<Integer> array;

    public ListDTO() {
    }
}
