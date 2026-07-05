package cli;

// Well well who came here... Future Pratik!? Sit man, let's chat.
// We created this class because Main was becoming huge.
// Keep responsibilities separate.
// Main should orchestrate, this class should only understand CLI arguments.

import java.util.ArrayList;
import java.util.List;

public final class CommandLineParser {
    private CommandLineParser() {
    }

    public static CommandLineOptions parse(String[] args) {
        boolean onlyMatching = false;
        ColorMode colorMode = ColorMode.NEVER;
        String pattern = null;
        List<String> fileNames = new ArrayList<>();
        boolean recursive = false;

        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("--color=")) {
                String mode = args[i].substring("--color=".length());
                colorMode = switch (mode) {
                    case "always" -> ColorMode.ALWAYS;
                    case "auto" -> ColorMode.AUTO;
                    case "never" -> ColorMode.NEVER;

                    default -> throw new IllegalArgumentException(
                            "Unknown color mode: " + mode);
                };

                continue;

            }

            switch (args[i]) {
                case "-o" -> onlyMatching = true;
                case "-r" -> recursive = true;
                case "-E" -> {
                    if (i + 1 >= args.length) {
                        throw new IllegalArgumentException(
                                "Missing pattern after -E");
                    }

                    pattern = args[++i];
                }
                default -> {
                    fileNames.add(args[i]);
                }
            }
        }
        if (pattern == null) {
            throw new IllegalArgumentException("Pattern is required.");
        }

        return new CommandLineOptions(
                onlyMatching,
                colorMode,
                pattern,
                fileNames,
                recursive
        );
    }
}