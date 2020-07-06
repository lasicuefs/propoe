package javafx.utilities;

public class LogPrintable {
    private static StringBuilder STRING_PRINT = new StringBuilder();

    public static String getLogString() {
        return STRING_PRINT.toString();
    }

    public static void stringBefore(String out) {
        STRING_PRINT.insert(0, out);
    }

    public static void log(String out) {
        STRING_PRINT.append(out);
        System.out.print(out);
    }

    public static void clean() {
        STRING_PRINT = new StringBuilder();
    }

    public static void logln(String out) {
        log(out + "\n");
    }
}
