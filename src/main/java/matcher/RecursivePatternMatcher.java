package matcher;

import model.MatchResult;
import model.Token;
import model.TokenType;

public final class RecursivePatternMatcher {

    private RecursivePatternMatcher() {
    }

    public static MatchResult findMatch(String input, String pattern) {
        Token firstToken = TokenReader.read(pattern, 0);

        if (firstToken.getType() == TokenType.START_ANCHOR) {
            MatchResult result = doesRemainingPatternMatchHere(input, 0, pattern, 0);

            if (result.getMatched()) {
                return new MatchResult(
                        true,
                        0,
                        result.getEndIdx()
                );
            }

            return result;
        }

        // s o here we will start the input val per increment and pattern will always start 0
        for (int start = 0; start < input.length(); start++) {
            MatchResult result = doesRemainingPatternMatchHere(
                    input,
                    start,
                    pattern,
                    0);
            if (result.getMatched()) {
                return new MatchResult(
                        true,
                        start,
                        result.getEndIdx()
                );
            }
        }
        return new MatchResult(false, -1, -1);
    }

    private static MatchResult doesRemainingPatternMatchHere(
            String input,
            int inputIdx,
            String pattern,
            int patternIdx) {

        if (patternIdx == pattern.length()) {
            return new MatchResult(
                    true,
                    -1,
                    inputIdx
            );
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
                    yield new MatchResult(false, -1, -1);
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
                    yield new MatchResult(false, -1, -1);
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
                    MatchResult result = doesRemainingPatternMatchHere(input, inputIdx, completePattern, 0);
                    if (result.getMatched()) {
                        yield result;
                    }
                }

                yield new MatchResult(false, -1, -1);
            }

            default -> {
                if (nextToken != null && nextToken.getType() == TokenType.QUESTION_MARK) {
                    int remainingPatternIdx = nextPatternIdx + nextToken.getLength();
                    MatchResult result = doesRemainingPatternMatchHere(
                            input,
                            inputIdx + 1,
                            pattern,
                            remainingPatternIdx);
                    if (inputIdx < input.length()
                            && CharacterMatcher.matches(
                            input.charAt(inputIdx),
                            currentToken)
                            && result.getMatched()) {

                        yield result;
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
                        yield new MatchResult(
                                false,
                                -1,
                                -1
                        );
                    }
                    int remainingPatternIdx =
                            nextPatternIdx + nextToken.getLength();

                    int candidateStartIdx = inputIdx + 1;

                    while (true) {
                        MatchResult result = doesRemainingPatternMatchHere(
                                input,
                                candidateStartIdx,
                                pattern,
                                remainingPatternIdx);
                        if (result.getMatched()) {
                            yield result;
                        }

                        if (candidateStartIdx >= input.length()
                                || !CharacterMatcher.matches(
                                input.charAt(candidateStartIdx),
                                currentToken)) {

                            break;
                        }

                        candidateStartIdx++;
                    }

                    yield new MatchResult(
                            false,
                            -1,
                            -1
                    );
                }

                if (inputIdx == input.length()) {
                    yield new MatchResult(
                            false,
                            -1,
                            -1
                    );
                }

                if (!CharacterMatcher.matches(
                        input.charAt(inputIdx),
                        currentToken)) {
                    yield new MatchResult(
                            false,
                            -1,
                            -1
                    );
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