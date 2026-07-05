import cli.Ansi;
import cli.ColorMode;
import cli.CommandLineOptions;
import cli.CommandLineParser;
import matcher.RecursivePatternMatcher;
import model.MatchResult;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    private static boolean shouldHighlight(ColorMode colorMode) {
        return switch (colorMode) {
            case ALWAYS -> true;
            case NEVER -> false;
            case AUTO -> System.console() != null;
        };
    }

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
            String fileName,
            boolean multipleFiles,
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

        String output = highlightOutput
                ? highlightAllMatches(inputLine, pattern)
                : inputLine;

        if (multipleFiles) {
            output = fileName + ":" + output;
        }

        System.out.println(output);

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

    // man need to change this coz need multi support file sys
//    private static Scanner createScanner(CommandLineOptions options) throws IOException {
//        if (options.getFileNames() == null) {
//            return new Scanner(System.in);
//        }
//
//        return new Scanner(new File(options.getFileNames()));
//    }

    private static boolean processScanner(
            Scanner scanner,
            CommandLineOptions options,
            String fileName) {

        boolean foundMatch = false;

        boolean multipleFiles =
                options.getFileNames().size() > 1;

        while (scanner.hasNextLine()) {

            String inputLine = scanner.nextLine();

            if (options.isOnlyMatching()) {

                foundMatch |= printEachMatch(
                        inputLine,
                        options.getPattern());

            } else {

                foundMatch |= printWholeLineIfMatched(
                        fileName,
                        multipleFiles,
                        inputLine,
                        options.getPattern(),
                        shouldHighlight(options.getColorMode()));
            }
        }

        return foundMatch;
    }
    public static void main(String[] args) throws IOException {

        CommandLineOptions options =
                CommandLineParser.parse(args);

        boolean foundMatch = false;

        if (options.getFileNames().isEmpty()) {

            foundMatch |= processScanner(
                    new Scanner(System.in),
                    options,
                    null);

        } else {

            for (String fileName : options.getFileNames()) {

                try (Scanner scanner =
                             new Scanner(new File(fileName))) {

                    foundMatch |= processScanner(
                            scanner,
                            options,
                            fileName);
                }
            }
        }

        System.exit(foundMatch ? 0 : 1);
    }
}