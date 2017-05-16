package classbytecode;

import java.util.List;

public class MappedByteCodeMethod {

    private String methodName;

    private List<String> bytecode;

    public MappedByteCodeMethod(String methodName) {
        this.methodName = methodName;
    }

    public MappedByteCodeMethod(String methodName, List<String> bytecode) {
        this.methodName = methodName;
        this.bytecode = bytecode;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public List<String> getBytecode() {
        return bytecode;
    }

    public void setBytecode(List<String> bytecode) {
        this.bytecode = bytecode;
    }
}
