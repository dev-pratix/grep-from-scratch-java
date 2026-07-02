package matcher;

import model.Token;
import model.TokenType;

public final class RecursivePatternMatcher {

    private RecursivePatternMatcher() {
    }

    public static boolean matches(String input, String pattern) {
        Token firstToken = TokenReader.read(pattern, 0);

        if (firstToken.getType() == TokenType.START_ANCHOR) {
            return doesRemainingPatternMatchHere(input, 0, pattern, 0);
        }

        // s o here we will start the input val per increment and pattern will always start 0
        for (int start = 0; start < input.length(); start++) {
            if (doesRemainingPatternMatchHere(
                    input,
                    start,
                    pattern,
                    0)) {

                return true;
            }
        }
        return false;
    }

    private static boolean doesRemainingPatternMatchHere(
            String input,
            int inputIdx,
            String pattern,
            int patternIdx) {

        if (patternIdx == pattern.length()) {
            return true;
        }
        Token currentToken = TokenReader.read(pattern, patternIdx);

        int nextPatternIdx = patternIdx + currentToken.getLength();

        Token nextToken = null;
        if (nextPatternIdx < pattern.length()) {
            nextToken = TokenReader.read(pattern, nextPatternIdx);
        }

        return switch (currentToken.getType()) {
            case START_ANCHOR -> {
                if (inputIdx != 0) {
                    yield false;
                }

                yield doesRemainingPatternMatchHere(
                        input,
                        inputIdx,
                        pattern,
                        nextPatternIdx
                );
            }
            case END_ANCHOR -> {
                if (inputIdx != input.length()) {
                    yield false;
                }
                yield doesRemainingPatternMatchHere(
                        input,
                        inputIdx,
                        pattern,
                        nextPatternIdx
                );
            }
            case ALTERNATION -> {
                String[] alternationStrings = currentToken.getValue().split("\\|");

                String remainingPattern = pattern.substring(nextPatternIdx);

                for (String alternationString : alternationStrings) {
                    String completePattern = alternationString + remainingPattern;
                    System.out.println("Trying: " + completePattern);
                    if (doesRemainingPatternMatchHere(input, inputIdx, completePattern, 0)) {
                        yield true;
                    }
                }

                yield false;
            }

            default -> {
                if (nextToken != null && nextToken.getType() == TokenType.QUESTION_MARK) {
                    int remainingPatternIdx = nextPatternIdx + nextToken.getLength();
                    if (inputIdx < input.length()
                            && CharacterMatcher.matches(
                            input.charAt(inputIdx),
                            currentToken)
                            && doesRemainingPatternMatchHere(
                            input,
                            inputIdx + 1,
                            pattern,
                            remainingPatternIdx)) {

                        yield true;
                    }

                    yield doesRemainingPatternMatchHere(
                            input,
                            inputIdx,
                            pattern,
                            remainingPatternIdx);
                }

                if (nextToken != null
                        && nextToken.getType() == TokenType.PLUS) {
                    if (!CharacterMatcher.matches(input.charAt(inputIdx), currentToken)) {
                        yield false;
                    }
                    int remainingPatternIdx =
                            nextPatternIdx + nextToken.getLength();

                    int candidateStartIdx = inputIdx + 1;

                    while (true) {
                        if (doesRemainingPatternMatchHere(
                                input,
                                candidateStartIdx,
                                pattern,
                                remainingPatternIdx)) {

                            yield true;
                        }

                        if (candidateStartIdx >= input.length()
                                || !CharacterMatcher.matches(
                                input.charAt(candidateStartIdx),
                                currentToken)) {

                            break;
                        }

                        candidateStartIdx++;
                    }

                    yield false;
                }

                if (inputIdx == input.length()) {
                    yield false;
                }

                if (!CharacterMatcher.matches(
                        input.charAt(inputIdx),
                        currentToken)) {
                    yield false;
                }

                yield doesRemainingPatternMatchHere(
                        input,
                        inputIdx + 1,
                        pattern,
                        nextPatternIdx
                );
            }
        };
    }
}