package cli;

// Well well who came here... Future Pratik !? Sit man let's chat... why do you think we create this class!?
//coz the the Main.class main method was so ugly i dont even want to touch it hope my future self
// is still the one who loves writing clean code and writing code for himself not for others....
//Don't forget the roots... you love typing code yourself don't let agent touch your project man don't be that you know what yes exactly i know you know ... we know...
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

                    pattern = args[i + 1];
                }

                default -> {
                    throw new IllegalArgumentException(
                            "Unknown argument: " + args[i]);
                }
            }
        }

        if (pattern == null) {
            throw new IllegalArgumentException(
                    "Pattern is required.");
        }

        return new CommandLineOptions(
                onlyMatching,
                highlight,
                pattern
        );
    }
}