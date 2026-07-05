import cli.*;
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
            builder.append(
                    inputLine,
                    searchStart,
                    result.getStartIdx());

            builder.append(Ansi.BOLD_RED)
                    .append(
                            inputLine,
                            result.getStartIdx(),
                            result.getEndIdx())
                    .append(Ansi.RESET);

            if (pattern.startsWith("^")) {
                searchStart = result.getEndIdx();
                break;
            }

            searchStart = result.getEndIdx();
        }
        builder.append(inputLine.substring(searchStart));
        return builder.toString();
    }

    /**
     * Prints the entire line if it matches.
     */
    private static boolean printWholeLineIfMatched(
            String source,
            boolean showSource,
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
        if (showSource) {
            output = source + ":" + output;
        }

        System.out.println(output);
        return true;
    }

    /**
     * grep -o
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

    private static boolean processScanner(
            Scanner scanner,
            CommandLineOptions options,
            String source) {
        boolean foundMatch = false;
        boolean showSource =
                options.isRecursive()
                        || options.getFileNames().size() > 1;
        while (scanner.hasNextLine()) {
            String inputLine = scanner.nextLine();
            if (options.isOnlyMatching()) {
                foundMatch |= printEachMatch(
                        inputLine,
                        options.getPattern());
            } else {
                foundMatch |= printWholeLineIfMatched(
                        source,
                        showSource,
                        inputLine,
                        options.getPattern(),
                        shouldHighlight(
                                options.getColorMode()));
            }
        }

        return foundMatch;
    }

    public static void main(String[] args) throws IOException {
        CommandLineOptions options =
                CommandLineParser.parse(args);
        boolean foundMatch = false;
        // stdin
        if (options.getFileNames().isEmpty()) {
            foundMatch |= processScanner(
                    new Scanner(System.in),
                    options,
                    null);
        }

        // recursive search
        else if (options.isRecursive()) {
            for (String directory : options.getFileNames()) {
                for (File file :
                        FileCollector.collect(new File(directory))) {
                    try (Scanner scanner =
                                 new Scanner(file)) {
                        foundMatch |= processScanner(
                                scanner,
                                options,
                                file.getPath());
                    }
                }
            }
        }

        // normal file search
        else {
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