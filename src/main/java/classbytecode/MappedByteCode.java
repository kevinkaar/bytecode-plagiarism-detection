package classbytecode;

import java.util.List;

public class MappedByteCode {

    private String classId;

    private String className;

    private List<MappedByteCodeMethod> methods;

    public MappedByteCode(String classId,
                          String className,
                          List<MappedByteCodeMethod> methods) {
        this.classId = classId;
        this.className = className;
        this.methods = methods;
    }

    public String getClassId() { return classId; }

    public List<MappedByteCodeMethod> getMethods() {
        return methods;
    }

    public String getClassName() {
        return className;
    }

}
