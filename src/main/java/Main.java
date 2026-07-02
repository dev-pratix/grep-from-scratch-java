import matcher.RecursivePatternMatcher;
import model.MatchResult;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        boolean onlyMatching = false;
        String pattern;

        if (args.length == 2 && args[0].equals("-E")) {
            pattern = args[1];

        } else if (args.length == 3
                && args[0].equals("-o")
                && args[1].equals("-E")) {

            onlyMatching = true;
            pattern = args[2];

        } else {
            System.out.println(
                    "Usage: ./your_program.sh [-o] -E <pattern>");
            System.exit(1);
            return;
        }

        Scanner scanner = new Scanner(System.in);
        boolean foundMatch = false;
        while (scanner.hasNextLine()) {
            String inputLine = scanner.nextLine();
            MatchResult result =
                    RecursivePatternMatcher.findMatch(
                            inputLine,
                            pattern);

            if (!result.getMatched()) {
                continue;
            }

            foundMatch = true;
            if (onlyMatching) {
                System.out.println(
                        inputLine.substring(
                                result.getStartIdx(),
                                result.getEndIdx()));

            } else {
                System.out.println(inputLine);

            }
        }

        System.exit(foundMatch ? 0 : 1);
    }
}