package cli;

// Well well who came here... Future Pratik!? Sit man, let's chat.
// We created this class because Main was becoming huge.
// Keep responsibilities separate.
// Main should orchestrate, this class should only understand CLI arguments.

public final class CommandLineParser {
    private CommandLineParser() {
    }

    public static CommandLineOptions parse(String[] args) {
        boolean onlyMatching = false;
        boolean highlight = false;
        String pattern = null;

        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("--color=")) {
                String colorMode = args[i].substring("--color=".length());
                switch (colorMode) {
                    case "always" -> highlight = true;
                    case "auto", "never" -> highlight = false;
                    default -> throw new IllegalArgumentException(
                            "Unknown color mode: " + colorMode);
                }

                continue;
            }

            switch (args[i]) {
                case "-o" -> onlyMatching = true;
                case "-E" -> {
                    if (i + 1 >= args.length) {
                        throw new IllegalArgumentException(
                                "Missing pattern after -E");
                    }

                    pattern = args[++i];
                }
                default -> throw new IllegalArgumentException(
                        "Unknown argument: " + args[i]);
            }
        }
        if (pattern == null) {
            throw new IllegalArgumentException("Pattern is required.");
        }

        return new CommandLineOptions(
                onlyMatching,
                highlight,
                pattern
        );
    }
}