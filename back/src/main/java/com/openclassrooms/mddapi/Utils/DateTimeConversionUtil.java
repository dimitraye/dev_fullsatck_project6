package com.openclassrooms.mddapi.Utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeConversionUtil {
    public static String convertLocalDateTimeToString(LocalDateTime localDateTime) {
        // Crée un formateur de date avec le format "yyyy/MM/dd"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

        // Utilisez le formateur pour convertir LocalDateTime en String
        String formattedDate = localDateTime.format(formatter);

        return formattedDate;
    }
}