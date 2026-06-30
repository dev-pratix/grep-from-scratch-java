package matcher;

public class PatternMatcher {

    public static boolean matches(String inputLine, String pattern) {
        return switch (pattern) {
            case "\\d" -> matchDigit(inputLine);
            case "\\w" -> matchWord(inputLine);
            default -> inputLine.contains(pattern);
        };
    }

    public static boolean matchDigit(String inputLine) {
        for (char c : inputLine.toCharArray()) {
            if (Character.isDigit(c)) return true;
        }

        return false;
    }

    public static boolean matchWord(String inputLine) {
        for (char c : inputLine.toCharArray()) {
            if (Character.isLetterOrDigit(c)) return true;
        }

        return false;
    }
}
