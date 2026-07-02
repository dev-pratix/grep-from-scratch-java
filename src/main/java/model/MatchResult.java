package model;

public class MatchResult {
    private final boolean matched;
    private final int startIdx;
    private final int endIdx;

    public MatchResult(boolean matched, int startIdx, int endIdx) {
        this.matched = matched;
        this.startIdx = startIdx;
        this.endIdx = endIdx;
    }

    public boolean getMatched() {
        return matched;
    }

    public int getStartIdx() {
        return startIdx;
    }

    public int getEndIdx() {
        return endIdx;
    }
}
