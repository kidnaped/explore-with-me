package ru.practicum.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Utils {
    public static final String FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern(FORMAT);
}
