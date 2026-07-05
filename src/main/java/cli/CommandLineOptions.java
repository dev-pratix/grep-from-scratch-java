package cli;

public class CommandLineOptions {
    private final ColorMode colorMode;
    private final boolean onlyMatches;
    private final String pattern;
    private final String fileName;

    public CommandLineOptions(
            boolean onlyMatches,
            ColorMode colorMode,
            String pattern,
            String fileName) {

        this.onlyMatches = onlyMatches;
        this.colorMode = colorMode;
        this.pattern = pattern;
        this.fileName = fileName;
    }

    public ColorMode getColorMode() {
        return colorMode;
    }

    public boolean isOnlyMatching() {
        return onlyMatches;
    }

    public String getPattern() {
        return pattern;
    }

    public String getFileName(){
        return fileName;
    }

}
