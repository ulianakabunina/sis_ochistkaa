import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {

        try {
            DebugLogger.log("Старт программы");

            File jFile = new File("TestFile.java");

            if (!jFile.exists()) {
                DebugLogger.log("Файл TestFile.java не найден!");
                return;
            }

            String code = Files.readString(jFile.toPath());
            DebugAnalyzer.analyzeCode(code);

            DebugAnalyzer.traceStep("Исходный код", code);

            code = delComm(code);
            DebugAnalyzer.traceStep("После удаления комментариев", code);

            code = newProb(code);
            DebugAnalyzer.traceStep("После нормализации пробелов", code);

            String oldClassName = findClass(code);
            DebugLogger.log("Найден класс: " + oldClassName);

            String newClassName = "A";
            code = code.replaceAll("\\b" + oldClassName + "\\b", newClassName);

            DebugAnalyzer.traceStep("После переименования класса", code);

            code = zamena(code, newClassName);

            DebugAnalyzer.traceStep("После замены идентификаторов", code);

            File newFile = new File(newClassName + ".java");
            Files.writeString(newFile.toPath(), code);

            DebugLogger.log("Готово! Файл сохранён как: " + newFile.getName());

        } catch (IOException e) {
            DebugLogger.error("Ошибка работы с файлом", e);
        } catch (Exception e) {
            DebugLogger.error("Неизвестная ошибка", e);
            e.printStackTrace();
        }
    }

    static String delComm(String s) {
        DebugLogger.log("Удаление комментариев");
        s = s.replaceAll("//.*", "");
        s = s.replaceAll("(?s)/\\*.*?\\*/", "");
        return s;
    }

    static String newProb(String s) {
        DebugLogger.log("Нормализация пробелов");
        s = s.replaceAll("\\s+", " ");
        s = s.replaceAll("\\s*([{}();=,+-])\\s*", "$1");
        return s.trim();
    }

    static String findClass(String code) {
        DebugLogger.log("Поиск имени класса");
        Matcher m = Pattern.compile("class\\s+(\\w+)").matcher(code);
        if (m.find()) return m.group(1);
        return "UnknownClass";
    }

    static String zamena(String code, String className) {
        DebugLogger.log("Начало замены идентификаторов");

        Set<String> ids = new HashSet<>();

        Matcher m = Pattern.compile("\\b[a-zA-Z_]\\w*\\b").matcher(code);
        while (m.find()) {
            String id = m.group();
            if (!id.equals(className) &&
                    !List.of("class","public","private","protected","static","void","int",
                            "double","float","return","new","if","else","for","while").contains(id)) {
                ids.add(id);
            }
        }

        DebugLogger.log("Найдено идентификаторов: " + ids.size());

        List<String> newNames = generateNames(ids.size());

        int i = 0;
        for (String old : ids) {
            DebugAnalyzer.traceStep("Замена", old + " -> " + newNames.get(i));
            code = code.replaceAll("\\b" + old + "\\b", newNames.get(i++));
        }

        return code;
    }

    static List<String> generateNames(int n) {
        DebugLogger.log("Генерация новых имён");

        List<String> list = new ArrayList<>();
        int count = 0;
        while (list.size() < n) {
            list.add(createName(count++));
        }
        return list;
    }

    static String createName(int i) {
        StringBuilder sb = new StringBuilder();
        do {
            sb.append((char)('a' + (i % 26)));
            i /= 26;
        } while (i > 0);
        return sb.toString();
    }
}