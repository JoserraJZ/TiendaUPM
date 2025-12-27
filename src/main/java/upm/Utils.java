package upm;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Utils {

    public static String formatDouble(double d) {
        BigDecimal bd = BigDecimal.valueOf(d)
                .setScale(5, RoundingMode.HALF_UP)
                .stripTrailingZeros();

        String s = bd.toPlainString();

        if (!s.contains(".")) {// Si es un entero, añadimos ".0"
            s += ".0";
        }
        return s;
    }

    public static String[] splitText(String text) {
        List<String> parts = new ArrayList<>();
        Matcher m = Pattern.compile("\"([^\"]*)\"|(\\S+)").matcher(text);
        while (m.find()) parts.add(m.group(1) != null ? m.group(1) : m.group(2));
        return parts.toArray(new String[0]);
    }

    //TODO: COMPROBAR SI ESTO ES NIF
    public static boolean isNIF(String str){
        return str.matches("[A-Z]\\d{7,8}");
    }
}
