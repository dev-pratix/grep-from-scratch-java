package matcher;

import model.Token;
import model.TokenType;

public class TokenReader {

    private TokenReader() {
    }

    public static Token read(String pattern, int patternIdx) {
        if (patternIdx >= pattern.length()) {
            throw new IllegalArgumentException(
                    "Invalid pattern index: " + patternIdx);
        }

        char current = pattern.charAt(patternIdx);
        return switch (current) {
            case '\\' -> readEscapeToken(pattern, patternIdx);
            case '[' -> readGroupToken(pattern, patternIdx);
            case '^' -> readStartAnchorToken();
            case '$' -> readEndAnchorToken();
            case '+' -> readPlusQuantifierToken();
            case '?' -> readQuestionQuantifierToken();
            default -> readLiteralToken(current);
        };
    }

    private static Token readStartAnchorToken() {
        return new Token(TokenType.START_ANCHOR, 1, "^");
    }

    private static Token readEndAnchorToken() {
        return new Token(TokenType.END_ANCHOR, 1, "$");
    }

    private static Token readPlusQuantifierToken() {
        return new Token(TokenType.PLUS, 1, "+");
    }

    private static Token readEscapeToken(String pattern, int patternIdx) {
        if (patternIdx + 1 >= pattern.length()) {
            throw new IllegalArgumentException("Incomplete escape sequence");
        }

        char escapedCharacter = pattern.charAt(patternIdx + 1);
        return switch (escapedCharacter) {

            case 'd' -> new Token(
                    TokenType.DIGIT,
                    2,
                    "\\d"
            );

            case 'w' -> new Token(
                    TokenType.WORD,
                    2,
                    "\\w"
            );

            default -> throw new IllegalArgumentException(
                    "Unknown escape sequence: \\" + escapedCharacter);
        };
    }

    private static Token readGroupToken(String pattern, int patternStartIdx) {
        int closingBracketIdx = pattern.indexOf(']', patternStartIdx);

        if (closingBracketIdx == -1) throw new IllegalArgumentException("Missing closing brackets");

        boolean isNegativeGroup = pattern.charAt(patternStartIdx + 1) == '^';
        int startIdx = isNegativeGroup ? patternStartIdx + 2 : patternStartIdx + 1; // coz i want to extract value only wihtout counting braces
        String value = pattern.substring(startIdx, closingBracketIdx);
        TokenType type = isNegativeGroup ? TokenType.NEGATIVE_GROUP : TokenType.POSITIVE_GROUP;
        return new Token(type, closingBracketIdx - patternStartIdx + 1, value);
    }

    // created this only for literals ok future pratikk
    private static Token readLiteralToken(Character c) {
        return new Token(TokenType.LITERAL, 1, String.valueOf(c));
    }

    private static Token readQuestionQuantifierToken() {
        return new Token(TokenType.QUESTION_MARK, 1, "?");
    }
}
