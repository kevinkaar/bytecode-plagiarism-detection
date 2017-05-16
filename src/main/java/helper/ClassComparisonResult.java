package helper;

import java.util.List;

public class ClassComparisonResult {

    private String classId1;

    private String classId2;

    private List<MethodComparisonResult> methodComparisonResult;

    public ClassComparisonResult(String classId1, String classId2) {
        this.classId1 = classId1;
        this.classId2 = classId2;
    }

    public String getClassId1() {
        return classId1;
    }

    public String getClassId2() {
        return classId2;
    }

    public List<MethodComparisonResult> getMethodComparisonResult() {
        return methodComparisonResult;
    }

    public void setMethodComparisonResult(List<MethodComparisonResult> methodComparisonResult) {
        this.methodComparisonResult = methodComparisonResult;
    }

}
