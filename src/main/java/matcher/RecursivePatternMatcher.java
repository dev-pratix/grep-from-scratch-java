package matcher;

import model.Token;

public class RecursivePatternMatcher {

    public static boolean matches(String input, String pattern) {
        return match(input, 0, pattern, 0);
    }

    public static boolean match(String input, int idx, String pattern, int pIdx) {

        if (pIdx == pattern.length()) {
            return idx == input.length();
        }

        Token currentToken = TokenReader.read(pattern, pIdx);
        return switch (currentToken.getType()) {
            case START_ANCHOR -> {
                if (idx != 0) {
                    yield false;
                }
                yield match(input, idx, pattern, pIdx + currentToken.getLength());
            }
            case END_ANCHOR -> {
                if (idx != input.length()) {
                    yield false;
                }

                yield match(input, idx, pattern, pIdx + currentToken.getLength());
            }
            default -> {
                if (idx == input.length()) {
                    yield false;
                }

                if (!CharacterMatcher.matches(
                        input.charAt(idx),
                        currentToken)) {

                    yield false;
                }
                yield match(input, idx + 1, pattern, pIdx + currentToken.getLength());
            }
        };
    }
}
