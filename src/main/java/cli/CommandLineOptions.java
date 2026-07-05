package cli;

import java.util.List;

public class CommandLineOptions {
    private final ColorMode colorMode;
    private final boolean onlyMatches;
    private final String pattern;
    private final List<String> fileNames;

    public CommandLineOptions(
            boolean onlyMatches,
            ColorMode colorMode,
            String pattern,
           List<String> fileNames) {

        this.onlyMatches = onlyMatches;
        this.colorMode = colorMode;
        this.pattern = pattern;
        this.fileNames = fileNames;
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

    public List<String> getFileNames(){
        return fileNames;
    }

}
