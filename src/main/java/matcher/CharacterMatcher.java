package matcher;

import model.Token;

public final class CharacterMatcher {

    private CharacterMatcher() {
    }

    public static boolean matches(char inputCharacter, Token token) {
        return switch (token.getType()) {
            case DIGIT -> matchDigit(inputCharacter);

            case WORD -> matchWord(inputCharacter);

            case POSITIVE_GROUP -> matchPositiveGroup(
                    inputCharacter,
                    token.getValue());

            case NEGATIVE_GROUP -> matchNegativeGroup(
                    inputCharacter,
                    token.getValue());

            case LITERAL -> matchLiteral(
                    inputCharacter,
                    token.getValue());
            case WILDCARD -> matchWildCard(inputCharacter);
            case START_ANCHOR, END_ANCHOR, PLUS, QUESTION_MARK, ALTERNATION -> throw new IllegalStateException(
                    "[" + token.getType() + "] should never reach CharacterMatcher");
        };
    }

    private static boolean matchWildCard(char c) {
        return c != '\n';
    }

    private static boolean matchDigit(char c) {
        return Character.isDigit(c);
    }

    private static boolean matchWord(char c) {
        return Character.isLetterOrDigit(c) || c == '_';
    }

    private static boolean matchLiteral(char c, String value) {
        return c == value.charAt(0);
    }

    private static boolean matchPositiveGroup(char c, String group) {
        return containsCharacter(group, c);
    }

    private static boolean matchNegativeGroup(char c, String group) {
        return !containsCharacter(group, c);
    }

    private static boolean containsCharacter(String group, char c) {

        for (int i = 0; i < group.length(); i++) {

            if (group.charAt(i) == c) {
                return true;
            }

        }

        return false;
    }
}