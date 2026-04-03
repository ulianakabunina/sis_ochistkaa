import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class DebugAnalyzer {

    public static void analyzeCode(String code) {
        if (!DebugLogger.DEBUG) return;

        DebugLogger.log("Анадиз кода");

        int length = code.length();
        int instructions = code.split(";").length;
        int lines = code.split("\n").length;

        DebugLogger.log("Длина кода: " + length);
        DebugLogger.log("Количество строк: " + lines);
        DebugLogger.log("Количество инструкций: " + instructions);

        countKeywords(code);
    }

    public static void traceStep(String step, String data) {
        if (!DebugLogger.DEBUG) return;

        DebugLogger.log("[TRACE] " + step + " -> " + shorten(data));
    }

    private static void countKeywords(String code) {
        String[] keywords = {
                "if", "else", "for", "while",
                "class", "return", "new", "static"
        };

        for (String key : keywords) {
            int count = countOccurrences(code, key);
            DebugLogger.log("Ключевое слово '" + key + "': " + count);
        }
    }

    private static int countOccurrences(String code, String word) {
        Matcher m = Pattern.compile("\\b" + word + "\\b").matcher(code);
        int count = 0;
        while (m.find()) count++;
        return count;
    }

    private static String shorten(String s) {
        if (s == null) return "null";

        int maxLength = 120;

        if (s.length() > maxLength) {
            return s.substring(0, maxLength) + "...";
        }

        return s;
    }
}