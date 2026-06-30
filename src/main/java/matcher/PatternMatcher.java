package matcher;

public class PatternMatcher {

    public static boolean matches(String input, String pattern) {

        if (pattern.equals("\\d")) {
            return CharacterClassMatcher.matchDigit(input);
        } else if (pattern.equals("\\w")) {
            return CharacterClassMatcher.matchWord(input);
        } else if (pattern.startsWith("[^")) {
            return CharacterClassMatcher.matchNegativeGroup(input, pattern);
        } else if (pattern.startsWith("[")) {
            return CharacterClassMatcher.matchPositiveGroup(input, pattern);
        }
        return matchLiteral(input, pattern);
    }

    private static boolean matchLiteral(String input, String pattern) {
        return input.contains(pattern);
    }

}
