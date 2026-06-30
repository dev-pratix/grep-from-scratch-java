package matcher;

public class CharacterClassMatcher {

    public static boolean matchPositiveGroup(String input, String pattern) {
        if (pattern.length() < 3) return false; // at least 3 args
        int startIdx = pattern.indexOf("[");
        int endIdx = pattern.indexOf("]");
        if (startIdx >= endIdx) return false;

        String extractedString = pattern.substring(startIdx + 1, endIdx);
        for (int i = 0; i < input.length(); i++) {
            for (int j = startIdx + 1; j < extractedString.length(); j++) {
                if (input.charAt(i) == pattern.charAt(j)) return true;
            }
        }
        return false;
    }

    public static boolean matchDigit(String input) {
        for (char c : input.toCharArray()) {
            if (Character.isDigit(c)) return true;
        }

        return false;
    }

    public static boolean matchWord(String input) {
        for (char c : input.toCharArray()) {
            if (Character.isLetterOrDigit(c) || c == '_') return true;
        }

        return false;
    }
}
