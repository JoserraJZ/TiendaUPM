package upm;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
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

    public static boolean runFromFile(String filePath) {
        File inputFile = new File(filePath);

        if (!inputFile.isFile()){
            System.err.println("Fichero no encontrado: " + filePath + ". Ejecutando modo interactivo.");
            return false;
        }

        System.setProperty("isfromfile", "true");


        try (Scanner fileScanner = new Scanner(inputFile)) {
            Tienda.commandLoop(fileScanner);
            return true;
        } catch (FileNotFoundException e) {
            System.err.println("Error abriendo el fichero: " + e.getMessage());
            return false;
        } finally {
            System.clearProperty("isfromfile");
        }
    }

}
