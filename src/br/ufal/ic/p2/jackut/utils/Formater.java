package br.ufal.ic.p2.jackut.utils;

import java.util.ArrayList;

public class Formater {
    public static String format(ArrayList<?> arrayList) {
        String formattedString = "{";
        for (int i = 0; i < arrayList.size(); i++) {
            formattedString += arrayList.get(i).toString();
            if (i != arrayList.size() - 1) {
                formattedString += ",";
            }
        }
        formattedString += "}";
        return formattedString;
    }
}
