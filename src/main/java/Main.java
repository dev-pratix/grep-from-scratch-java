import matcher.RecursivePatternMatcher;
import model.MatchResult;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage: ./your_program.sh -E <pattern>");
            System.exit(1);
        }
        String pattern = "";
        if (args[0].equals("-E"))
            pattern = args[1];

        if (args[0].equals("-o"))
            pattern = args[2];

        // You can use print statements as follows for debugging, they'll be visible when running tests.
        System.err.println("Logs from your program will appear here!");
        boolean foundMatch = false;
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String inputLine = scanner.nextLine();
            MatchResult result = matchPattern(inputLine, pattern);
            if (result.getMatched()) {
                System.out.println(inputLine);
                foundMatch = true;
            }
        }

        System.exit(foundMatch ? 0 : 1);
    }

    public static MatchResult matchPattern(String inputLine, String pattern) {
        if (!pattern.isEmpty()) {
            return RecursivePatternMatcher.findMatch(inputLine, pattern);
        } else {
            throw new RuntimeException("Unhandled pattern: " + pattern);
        }
    }
}
