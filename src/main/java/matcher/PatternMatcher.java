package matcher;

import model.Token;

public final class PatternMatcher {

    private PatternMatcher() {
    }

    public static boolean matches(String input, String pattern) {
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