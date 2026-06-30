package matcher;

public class CharacterClassMatcher {

    public static boolean matchPositiveGroup(String input, String pattern) {
        int startIdx = pattern.indexOf("[");
        int endIdx = pattern.indexOf("]");
        if (startIdx >= endIdx) return false;

        String extractedString = pattern.substring(startIdx, endIdx);
        for (int i = 0; i < input.length(); i++) {
            for (int j = 0; j < extractedString.length(); j++) {
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
