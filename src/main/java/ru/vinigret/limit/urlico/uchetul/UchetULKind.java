package ru.vinigret.limit.urlico.uchetul;

import java.util.Map;
import java.util.TreeMap;

enum UchetULKind {
    SVOD,
    KAZEN,
    BIUDJ,
    AVTON;
    public static Map<Integer, String> returnMap(){
        Map<Integer, String> map = new TreeMap<>();
        map.put(0,"Сводная группа");
        map.put(1,"Казенные учреждения");
        map.put(2,"Бюджетные учреждения");
        map.put(3,"Автонономные учреждения");
        return map;
    }
    public static UchetULKind forInt(int id) {
        for (UchetULKind d : values()) {
            if (d.ordinal() == id) {
                return d;
            }
        }
        return SVOD;
    }
}
