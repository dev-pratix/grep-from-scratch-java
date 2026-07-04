import cli.Ansi;
import cli.CommandLineOptions;
import cli.CommandLineParser;
import matcher.RecursivePatternMatcher;
import model.MatchResult;

import java.util.Scanner;

public class Main {

    /**
     * Builds a new string with every regex match highlighted.
     */
    private static String highlightAllMatches(
            String inputLine,
            String pattern) {
        StringBuilder builder = new StringBuilder();
        int searchStart = 0;
        while (true) {
            MatchResult result =
                    RecursivePatternMatcher.findMatch(
                            inputLine,
                            pattern,
                            searchStart);
            if (!result.getMatched()) {
                break;
            }

            // Append text before the match
            builder.append(
                    inputLine,
                    searchStart,
                    result.getStartIdx());

            // Append highlighted match
            builder.append(Ansi.BOLD_RED)
                    .append(
                            inputLine,
                            result.getStartIdx(),
                            result.getEndIdx())
                    .append(Ansi.RESET);

            // Prevent infinite loop for anchored patterns
            if (pattern.startsWith("^")) {
                searchStart = result.getEndIdx();
                break;
            }

            searchStart = result.getEndIdx();
        }

        // Append whatever remains after the final match
        builder.append(inputLine.substring(searchStart));
        return builder.toString();
    }

    /**
     * Normal grep.
     * <p>
     * Prints the whole line if it contains a match.
     */
    private static boolean printWholeLineIfMatched(
            String inputLine,
            String pattern,
            boolean highlightOutput) {

        MatchResult result =
                RecursivePatternMatcher.findMatch(
                        inputLine,
                        pattern);

        if (!result.getMatched()) {
            return false;
        }

        if (highlightOutput) {
            System.out.println(
                    highlightAllMatches(
                            inputLine,
                            pattern));
        } else {
            System.out.println(inputLine);
        }

        return true;
    }

    /**
     * grep -o
     * <p>
     * Prints every match on its own line.
     */
    private static boolean printEachMatch(
            String inputLine,
            String pattern) {

        boolean found = false;
        int searchStart = 0;

        while (true) {

            MatchResult result =
                    RecursivePatternMatcher.findMatch(
                            inputLine,
                            pattern,
                            searchStart);

            if (!result.getMatched()) {
                break;
            }

            System.out.println(
                    inputLine.substring(
                            result.getStartIdx(),
                            result.getEndIdx()));

            found = true;

            if (pattern.startsWith("^")) {
                break;
            }

            searchStart = result.getEndIdx();
        }

        return found;
    }

    public static void main(String[] args) {
        CommandLineOptions options =
                CommandLineParser.parse(args);

        Scanner scanner = new Scanner(System.in);

        boolean foundMatch = false;

        while (scanner.hasNextLine()) {

            String inputLine = scanner.nextLine();

            if (options.isOnlyMatching()) {
                foundMatch |= printEachMatch(
                        inputLine,
                        options.getPattern());
            } else {
                foundMatch |= printWholeLineIfMatched(
                        inputLine,
                        options.getPattern(),
                        options.getHighlight());
            }
        }

        System.exit(foundMatch ? 0 : 1);
    }
}