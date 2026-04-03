import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

class DebugLogger {

    private static final String LOG_FILE = "debug.log";

    public static boolean DEBUG = true;

    public static void log(String message) {
        if (!DEBUG) return;

        String logMessage = format(message);
        System.out.println(logMessage);
        writeToFile(logMessage);
    }

    public static void error(String message, Exception e) {
        String logMessage = format("ERROR: " + message + " | " + e.getMessage());
        System.out.println(logMessage);
        writeToFile(logMessage);
    }

    private static String format(String message) {
        return "[" + java.time.LocalDateTime.now() + "] " + message;
    }

    private static void writeToFile(String message) {
        if (!DEBUG) return;

        try (java.io.FileWriter fw = new java.io.FileWriter(LOG_FILE, true)) {
            fw.write(message + "\n");
        } catch (Exception e) {
            System.out.println("Ошибка записи в лог!");
        }
    }
}