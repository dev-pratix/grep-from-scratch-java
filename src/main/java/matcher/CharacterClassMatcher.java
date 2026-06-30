package matcher;

public class CharacterClassMatcher {

    private static String extractGroupCharacters(String pattern) {
        if (!pattern.startsWith("[") || !pattern.endsWith("]")) {
            throw new IllegalArgumentException("Invalid character group: " + pattern);
        }

        int start = pattern.startsWith("[^") ? 2 : 1;

        return pattern.substring(start, pattern.length() - 1);
    }

    private static boolean containsCharacter(String group, char c) {
        for (int i = 0; i < group.length(); i++) {
            if (group.charAt(i) == c) {
                return true;
            }
        }

        return false;
    }

    public static boolean matchPositiveGroup(String input, String pattern) {
        String group = extractGroupCharacters(pattern);
        for (int i = 0; i < input.length(); i++) {
            if (containsCharacter(group, input.charAt(i))) return true;
        }
        return false;
    }


    public static boolean matchNegativeGroup(String input, String pattern) {
        String group = extractGroupCharacters(pattern);
        for (int i = 0; i < input.length(); i++) {
            if (!containsCharacter(group, input.charAt(i))) return true;
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
