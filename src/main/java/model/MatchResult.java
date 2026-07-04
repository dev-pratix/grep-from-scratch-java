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

    public static MatchResult match(int startIdx, int endIdx){
        return new MatchResult(true,startIdx,endIdx);
    }

    public static MatchResult noMatch() {
        return new MatchResult(false, -1, -1);
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
