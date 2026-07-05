package matcher;

import model.MatchResult;
import model.Token;
import model.TokenType;

/**
 * Recursive regex engine.
 * <p>
 * Given an input string and a pattern, recursively determines whether
 * the remaining pattern matches the remaining input.
 * <p>
 * This class is intentionally recursive because regex matching naturally
 * forms a recursive/backtracking problem.
 */
public final class RecursivePatternMatcher {

    private RecursivePatternMatcher() {
    }

    /**
     * Finds the first occurrence of the pattern in the input.
     * <p>
     * Search always begins from index 0.
     */
    public static MatchResult findMatch(String input, String pattern) {
        return findMatch(input, pattern, 0);
    }

    /**
     * Searches for the first match starting from searchStart.
     * <p>
     * Used by:
     * - normal grep (searchStart = 0)
     * - grep -o (continue searching after previous match)
     */
    public static MatchResult findMatch(String input, String pattern, int searchStart) {
        Token firstToken = TokenReader.read(pattern, 0);
        if (firstToken.getType() == TokenType.START_ANCHOR) {
            MatchResult result = doesRemainingPatternMatchHere(input, 0, pattern, 0);

            if (result.getMatched()) {
                return new MatchResult(true, 0, result.getEndIdx()
                );
            }

            return result;
        }

        // Try matching the pattern starting at every possible
// position until a complete match is found.
        for (int start = searchStart; start < input.length(); start++) {
            MatchResult result = doesRemainingPatternMatchHere(input, start, pattern, 0);
            if (result.getMatched()) {
                return MatchResult.match(start, result.getEndIdx());
            }
        }
        return MatchResult.noMatch();
    }

    private static MatchResult doesRemainingPatternMatchHere(
            String input,
            int inputIdx,
            String pattern,
            int patternIdx) {
        if (patternIdx == pattern.length()) {
            return MatchResult.match(-1, inputIdx);
        }

        Token currentToken = TokenReader.read(pattern, patternIdx);
        int nextPatternIdx = patternIdx + currentToken.getLength();
        Token nextToken =
                nextPatternIdx < pattern.length()
                        ? TokenReader.read(pattern, nextPatternIdx)
                        : null;

        return switch (currentToken.getType()) {
            case START_ANCHOR -> {
                if (inputIdx != 0) {
                    yield MatchResult.noMatch();
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
                    yield MatchResult.noMatch();
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

                yield MatchResult.noMatch();
            }

            default -> {
                if (nextToken != null && nextToken.getType() == TokenType.QUESTION_MARK) {
                    yield handleQuestionMark(input, inputIdx, pattern, nextPatternIdx, currentToken, nextToken);
                }

                if (nextToken != null && nextToken.getType() == TokenType.STAR) {
                    yield handleStar(input, inputIdx, pattern, nextPatternIdx, currentToken, nextToken);
                }

                if (nextToken != null
                        && nextToken.getType() == TokenType.PLUS) {
                    yield handlePlus(input, inputIdx, pattern, nextPatternIdx, currentToken, nextToken);
                }

                if (inputIdx == input.length()) {
                    yield MatchResult.noMatch();
                }

                if (!CharacterMatcher.matches(
                        input.charAt(inputIdx),
                        currentToken)) {
                    yield MatchResult.noMatch();
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

    private static MatchResult handleQuestionMark(
            String input,
            int inputIdx,
            String pattern,
            int nextPatternIdx,
            Token currentToken,
            Token nextToken) {

        int remainingPatternIdx =
                nextPatternIdx + nextToken.getLength();

        // Try consuming the optional character.
        if (inputIdx < input.length()
                && CharacterMatcher.matches(
                input.charAt(inputIdx),
                currentToken)) {

            MatchResult result =
                    doesRemainingPatternMatchHere(
                            input,
                            inputIdx + 1,
                            pattern,
                            remainingPatternIdx);

            if (result.getMatched()) {
                return result;
            }
        }

        // Try skipping it.
        return doesRemainingPatternMatchHere(
                input,
                inputIdx,
                pattern,
                remainingPatternIdx);
    }

    private static MatchResult handleStar(
            String input,
            int inputIdx,
            String pattern,
            int nextPatternIdx,
            Token currentToken,
            Token nextToken) {

        int remainingPatternIdx =
                nextPatternIdx + nextToken.getLength();

        // Try matching zero occurrences.
        MatchResult result =
                doesRemainingPatternMatchHere(
                        input,
                        inputIdx,
                        pattern,
                        remainingPatternIdx);

        if (result.getMatched()) {
            return result;
        }

        // Now try 1, 2, 3... occurrences.
        int candidateStartIdx = inputIdx;

        while (candidateStartIdx < input.length()
                && CharacterMatcher.matches(
                input.charAt(candidateStartIdx),
                currentToken)) {

            candidateStartIdx++;

            result = doesRemainingPatternMatchHere(
                    input,
                    candidateStartIdx,
                    pattern,
                    remainingPatternIdx);

            if (result.getMatched()) {
                return result;
            }
        }

        return MatchResult.noMatch();
    }

    private static MatchResult handlePlus(
            String input,
            int inputIdx,
            String pattern,
            int nextPatternIdx,
            Token currentToken,
            Token nextToken) {
        if (inputIdx == input.length()) {
            return MatchResult.noMatch();
        }
        if (!CharacterMatcher.matches(input.charAt(inputIdx), currentToken)) {
            return MatchResult.noMatch();
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
                return result;
            }

            if (candidateStartIdx >= input.length()
                    || !CharacterMatcher.matches(
                    input.charAt(candidateStartIdx),
                    currentToken)) {

                break;
            }
            candidateStartIdx++;
        }
        return MatchResult.noMatch();
    }
}