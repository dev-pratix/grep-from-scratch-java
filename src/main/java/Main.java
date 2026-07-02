import matcher.RecursivePatternMatcher;
import model.MatchResult;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        boolean onlyMatching = false;
        String pattern;

        if (args.length == 2 && "-E".equals(args[0])) {

            pattern = args[1];

        } else if (args.length == 3
                && "-o".equals(args[0])
                && "-E".equals(args[1])) {

            onlyMatching = true;
            pattern = args[2];

        } else {

            System.out.println("Usage: ./your_program.sh [-o] -E <pattern>");
            System.exit(1);
            return;
        }

        Scanner scanner = new Scanner(System.in);

        boolean foundMatch = false;

        while (scanner.hasNextLine()) {

            String inputLine = scanner.nextLine();

            if (!onlyMatching) {

                MatchResult result =
                        RecursivePatternMatcher.findMatch(
                                inputLine,
                                pattern);

                if (result.getMatched()) {
                    System.out.println(inputLine);
                    foundMatch = true;
                }

                continue;
            }

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

                foundMatch = true;
                if (pattern.startsWith("^")) {
                    break;
                }
                searchStart = result.getEndIdx();
            }
        }

        System.exit(foundMatch ? 0 : 1);
    }
}