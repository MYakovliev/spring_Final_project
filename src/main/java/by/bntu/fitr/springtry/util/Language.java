package by.bntu.fitr.springtry.util;

import java.util.Arrays;

/**
 * Enum contains languages supported by site
 */
public enum Language {
    RU,
    EN;

    public static boolean contains(String lang){
        return Arrays.stream(Language.values())
                .anyMatch(language ->
                        language.name()
                                .equals(lang.toUpperCase()));

    }
}
