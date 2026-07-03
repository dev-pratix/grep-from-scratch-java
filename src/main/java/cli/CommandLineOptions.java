package cli;

public class CommandLineOptions {
    private final boolean highlight;
    private final boolean onlyMatches;
    private final String pattern;

    public CommandLineOptions(
            boolean onlyMatches,
            boolean highlight,
            String pattern) {

        this.onlyMatches = onlyMatches;
        this.highlight = highlight;
        this.pattern = pattern;
    }

    public boolean getHighlight() {
        return highlight;
    }

    public boolean isOnlyMatching() {
        return onlyMatches;
    }

    public String getPattern() {
        return pattern;
    }

}
