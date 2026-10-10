package com.example.stroyopttorg.util;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/** Nomdan URL uchun slug yasaydi (kirill -> lotin transliteratsiya). */
public final class SlugUtil {
    private static final Map<Character, String> MAP = new HashMap<>();

    static {
        String[] ru = {"а", "a", "б", "b", "в", "v", "г", "g", "д", "d", "е", "e", "ё", "e", "ж", "zh",
                "з", "z", "и", "i", "й", "y", "к", "k", "л", "l", "м", "m", "н", "n", "о", "o", "п", "p",
                "р", "r", "с", "s", "т", "t", "у", "u", "ф", "f", "х", "kh", "ц", "ts", "ч", "ch", "ш", "sh",
                "щ", "shch", "ъ", "", "ы", "y", "ь", "", "э", "e", "ю", "yu", "я", "ya"};
        for (int i = 0; i < ru.length; i += 2) {
            MAP.put(ru[i].charAt(0), ru[i + 1]);
        }
    }

    private SlugUtil() {
    }

    public static String slugify(String input) {
        if (input == null) {
            return "item-" + Long.toString(System.currentTimeMillis(), 36);
        }
        StringBuilder sb = new StringBuilder();
        for (char ch : input.toLowerCase(Locale.ROOT).toCharArray()) {
            if (MAP.containsKey(ch)) {
                sb.append(MAP.get(ch));
            } else if ((ch >= 'a' && ch <= 'z') || (ch >= '0' && ch <= '9')) {
                sb.append(ch);
            } else {
                sb.append('-');
            }
        }
        String s = sb.toString().replaceAll("-{2,}", "-").replaceAll("^-|-$", "");
        return s.isEmpty() ? "item-" + Long.toString(System.currentTimeMillis(), 36) : s;
    }
}
