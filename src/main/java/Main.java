import cli.Ansi;
import cli.CommandLineOptions;
import cli.CommandLineParser;
import matcher.RecursivePatternMatcher;
import model.MatchResult;

import java.util.Scanner;

public class Main {

    private static String highlight(String input, MatchResult result, Ansi color) {
        String before = input.substring(0, result.getStartIdx());
        String matched = input.substring(result.getStartIdx(), result.getEndIdx());
        String after = input.substring(result.getEndIdx());

        return before + color + matched + Ansi.RESET + after;
    }

    private static boolean printMatchingLine(String inputLine, String pattern, boolean highlightOutput) {
        MatchResult result = RecursivePatternMatcher.findMatch(inputLine, pattern);
        if (!result.getMatched()) {
            return false;
        }

        if (highlightOutput) {
            System.out.println(highlight(inputLine, result, Ansi.BOLD_RED));
        } else {
            System.out.println(inputLine);
        }

        return true;
    }

    private static boolean printOnlyMatches(String inputLine, String pattern) {
        boolean found = false;
        int searchStart = 0;

        while (true) {
            MatchResult result = RecursivePatternMatcher.findMatch(inputLine, pattern, searchStart);
            if (!result.getMatched()) {
                break;
            }

            System.out.println(inputLine.substring(result.getStartIdx(), result.getEndIdx()));
            found = true;

            if (pattern.startsWith("^")) {
                break;
            }

            searchStart = result.getEndIdx();
        }

        return found;
    }


    public static void main(String[] args) {
        CommandLineOptions options = CommandLineParser.parse(args);

        Scanner scanner = new Scanner(System.in);

        boolean foundMatch = false;
        while (scanner.hasNext()) {
            String inputLine = scanner.nextLine();
            if (options.isOnlyMatching()) {
                foundMatch |= printOnlyMatches(inputLine, options.getPattern());
            } else {
                foundMatch |= printMatchingLine(inputLine, options.getPattern(), options.getHighlight());
            }
        }
        System.exit(foundMatch ? 0 : 1);
    }
}