package cli;

public class CommandLineOptions {
    private final boolean highlight;
    private final boolean onlyMatches;
    private final String pattern;

    public CommandLineOptions(boolean highlight, boolean onlyMatches, String pattern) {
        this.highlight = highlight;
        this.onlyMatches = onlyMatches;
        this.pattern = pattern;
    }

    public boolean getHighlight() {
        return highlight;
    }

    public boolean getOnlyMatches() {
        return onlyMatches;
    }

    public String getPattern() {
        return pattern;
    }

}
