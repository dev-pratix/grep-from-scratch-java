package matcher;

public final class PatternMatcher {

    public static boolean patternMatcher(String inputLine, String pattern) {
        if (pattern.equals("\\d")) {
            return isDecimalDigitCharacter(inputLine);
        }

        return inputLine.contains(pattern);
    }

    public static boolean isDecimalDigitCharacter(String inputLine) {
        for (char c : inputLine.toCharArray()) {
            if (Character.isDigit(c)) return true;
        }

        return false;
    }
}
