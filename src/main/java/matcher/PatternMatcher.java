package matcher;

import model.Token;
import model.TokenType;

public final class PatternMatcher {

    private PatternMatcher() {
    }

    public static boolean matches(String input, String pattern) {

        Token firstToken = TokenReader.read(pattern, 0);

        if (firstToken.getType() == TokenType.START_ANCHOR) {
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

            Token currentToken = TokenReader.read(pattern, patternIdx);
            if (currentToken.getType() == TokenType.START_ANCHOR) {
                patternIdx += currentToken.getLength();
                continue;
            }

            if (currentToken.getType() == TokenType.END_ANCHOR) {
                return inputIdx == input.length();
            }

            if (inputIdx >= input.length()) {
                return false;
            }

            if (!CharacterMatcher.matches(
                    input.charAt(inputIdx),
                    currentToken)) {
                return false;
            }

            inputIdx++;
            patternIdx += currentToken.getLength();
        }

        return true;
    }
}