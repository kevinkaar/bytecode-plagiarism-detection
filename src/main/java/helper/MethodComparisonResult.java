package helper;

public class MethodComparisonResult {

    private String method1;

    private String method2;

    private long totalInstructions;

    private long matchingInstructions;

    private double shorterInLongerRatio;

    public MethodComparisonResult(String method1, String method2) {
        this.method1 = method1;
        this.method2 = method2;
    }

    public String getMethod1() {
        return method1;
    }

    public void setMethod1(String method1) {
        this.method1 = method1;
    }

    public String getMethod2() {
        return method2;
    }

    public void setMethod2(String method2) {
        this.method2 = method2;
    }

    public long getTotalInstructions() {
        return totalInstructions;
    }

    public void setTotalInstructions(long totalInstructions) {
        this.totalInstructions = totalInstructions;
    }

    public long getMatchingInstructions() {
        return matchingInstructions;
    }

    public void setMatchingInstructions(long matchingInstructions) {
        this.matchingInstructions = matchingInstructions;
    }

    public double getShorterInLongerRatio() {
        return shorterInLongerRatio;
    }

    public void setShorterInLongerRatio(double shorterInLongerRatio) {
        this.shorterInLongerRatio = shorterInLongerRatio;
    }
}
