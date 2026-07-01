package matcher;

import model.Token;
import model.TokenType;
/**
 * First iterative regex engine.
 *
 * Supports:
 * - Literals
 * - Character classes
 * - Character groups
 * - Anchors
 *
 * Doesn't support backtracking-based quantifiers.
 */
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

            Token nextToken = null;
            if (pattern.length() > patternIdx + currentToken.getLength()) {
                nextToken = TokenReader.read(pattern, patternIdx + currentToken.getLength());
            }


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

            if (nextToken != null && nextToken.getType() == TokenType.PLUS) {
                inputIdx = consumeOneOrMoreGreedily(input, inputIdx, currentToken);
                patternIdx += currentToken.getLength();
                patternIdx += nextToken.getLength();
                continue;
            }

            inputIdx++;
            patternIdx += currentToken.getLength();
        }

        return true;
    }

    public static int consumeOneOrMoreGreedily(String input, int startIdx, Token currentToken) {
        startIdx++;
        while (startIdx < input.length() && CharacterMatcher.matches(input.charAt(startIdx), currentToken)) {
            startIdx++;
        }
        return startIdx;
    }
}