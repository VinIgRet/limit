package ru.vinigret.limit.pribor;

import java.util.Map;
import java.util.TreeMap;

public enum PriborKind {
    RASCHET,
    TEPLO,
    VODA,
    ELECTRO;
    public Map<Integer, String> returnMap(){
        Map<Integer, String> map = new TreeMap<>();
        map.put(0,"Прибор не используется");
        map.put(1,"Теплосчетчик");
        map.put(2,"Водосчетчик");
        map.put(3,"Электросчетчик");
        return map;
    }
}
