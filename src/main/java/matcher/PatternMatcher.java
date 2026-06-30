package matcher;

import model.Token;
import model.TokenType;

public final class PatternMatcher {

    private PatternMatcher() {
    }

    public static boolean matches(String input, String pattern) {
        Token token = TokenReader.read(pattern, 0);
        if (token.getType() == TokenType.START_ANCHOR) {
            return matchFromPosition(input, pattern, 0);
        }

        for (int start = 0; start < input.length(); start++) {
            if (matchFromPosition(input, pattern, start)) {
                return true;
            }

        }

        return false;
    }

    private static boolean matchFromPosition(
            String input,
            String pattern,
            int start) {

        int inputIdx = start;
        int patternIdx = 0;

        while (patternIdx < pattern.length()) {

            if (inputIdx >= input.length()) {
                return false;
            }

            Token token = TokenReader.read(pattern, patternIdx);

            if (token.getType() == TokenType.START_ANCHOR) {
                patternIdx+=token.getLength();
                continue;
            }

            if (!CharacterMatcher.matches(
                    input.charAt(inputIdx),
                    token)) {
                return false;
            }

            inputIdx++;
            patternIdx += token.getLength();
        }

        return true;
    }
}