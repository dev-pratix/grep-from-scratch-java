import matcher.PatternMatcher;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        if (args.length != 2 || !args[0].equals("-E")) {
            System.out.println("Usage: ./your_program.sh -E <pattern>");
            System.exit(1);
        }

        String pattern = args[1];
        Scanner scanner = new Scanner(System.in);
        String inputLine = scanner.nextLine();

        // You can use print statements as follows for debugging, they'll be visible when running tests.
        System.err.println("Logs from your program will appear here!");

        // TODO: Uncomment the code below to pass the first stage
        //
        if (matchPattern(inputLine, pattern)) {
            System.out.println("Exit 0");
            System.exit(0);
        } else {
            System.out.println("Exit 1");
            System.exit(1);
        }
    }

    public static boolean matchPattern(String inputLine, String pattern) {
        if (!pattern.isEmpty()) {
            return PatternMatcher.matches(inputLine, pattern);
        } else {
            throw new RuntimeException("Unhandled pattern: " + pattern);
        }
    }
}
