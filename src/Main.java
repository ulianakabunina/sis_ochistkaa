import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class Main {

    public static void main(String[] args) {

        try {
            File jFile = new File("TestFile.java");

            if (!jFile.exists()) {
                System.out.println("Файл TestFile.java не найден в папке проекта!");
                return;
            }

            String code = Files.readString(jFile.toPath());

            code = delComm(code);
            code = newProb(code);
            String oldClassName = findClass(code);
            String newClassName = "A";
            code = code.replaceAll("\\b" + oldClassName + "\\b", newClassName);
            code = zamena(code, newClassName);

            File newFile = new File(newClassName + ".java");
            Files.writeString(newFile.toPath(), code);

            System.out.println("Готово! Файл сохранён как: " + newFile.getName());

        } catch (IOException e) {
            System.out.println("Ошибка работы с файлом: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Произошла ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }

    static String delComm(String s) {
        s = s.replaceAll("//.*", "");
        s = s.replaceAll("(?s)/\\*.*?\\*/", "");
        return s;
    }

    static String newProb(String s) {
        s = s.replaceAll("\\s+", " ");
        s = s.replaceAll("\\s*([{}();=,+-])\\s*", "$1");
        return s.trim();
    }

    static String findClass(String code) {
        Matcher m = Pattern.compile("class\\s+(\\w+)").matcher(code);
        if (m.find()) return m.group(1);
        return "UnknownClass";
    }

    static String zamena(String code, String className) {
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

        List<String> newNames = generateNames(ids.size());

        int i = 0;
        for (String old : ids) {
            code = code.replaceAll("\\b" + old + "\\b", newNames.get(i++));
        }

        return code;
    }

    static List<String> generateNames(int n) {
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
