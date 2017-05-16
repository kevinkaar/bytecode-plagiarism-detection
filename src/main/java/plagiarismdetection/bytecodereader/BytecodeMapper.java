package plagiarismdetection.bytecodereader;

import classbytecode.MappedByteCodeMethod;

import java.util.*;

public class BytecodeMapper {

    private static final int MIN_BYTECODE_INSTRUCTIONS_TO_COMPARE = 3;


    private static final String PUBLIC_CLASS = "public class ";

    private static final String CODE = "Code:";

    private static final String CLASS_START = "{";

    private static final String CLASS_END = "}";

    private static final String PARANTHESIS_START = "(";

    private static final String PARANTHESIS_END = ")";

    private static final String EMPTY_STRING = "";

    private static final String PUBLIC = "public ";

    public String getClassNameFromBytecode(String bytecode) {

        int classNameIdx = bytecode.indexOf(PUBLIC_CLASS) + PUBLIC_CLASS.length();
        String bytecodeStartingFromClassName = bytecode.substring(classNameIdx);

        return bytecodeStartingFromClassName.substring(0, bytecodeStartingFromClassName.indexOf(" "));
    }

    public List<MappedByteCodeMethod> getMethodsFromByteCode(String bytecode, String className) {
        List<MappedByteCodeMethod> methods = new ArrayList<>();
        boolean isMethodName = false;

        String[] lines = bytecode.split("\n");
        for (int i = 0; i < lines.length - 1; i++) {
            if (isMethodName) {
                String methodName = lines[i].trim().replaceAll(";", "");

                String byteCodeStartingFromMethodCode = bytecode.substring(bytecode.indexOf(methodName) + methodName.length()).replaceAll(";", "");

                List<String> bytecodeInstructions = getBytecodeInstructions(byteCodeStartingFromMethodCode.trim());
                if (bytecodeInstructions.size() > MIN_BYTECODE_INSTRUCTIONS_TO_COMPARE) {
                    methods.add(new MappedByteCodeMethod(methodName, bytecodeInstructions));
                }
                isMethodName = false;
            }

            //If there are no fields in class there is no empty line string before first constructor
            if (isClassStartAndConstructorNext(lines[i], lines[i + 1], className) || isEmptyLineAndNextIsMethodNotField(lines[i], lines[i + 1])) isMethodName = true;
        }

        return methods;
    }

    private boolean isEmptyLineAndNextIsMethodNotField(String currentLine, String nextLine) {
        return currentLine.trim().equals(EMPTY_STRING) && (nextLine.contains(PARANTHESIS_START) && nextLine.contains(PARANTHESIS_END));
    }

    private boolean isClassStartAndConstructorNext(String currentLine, String nextLine, String className) {
        return currentLine.contains(CLASS_START) && nextLine.contains(PUBLIC + className);
    }

    private List<String> getBytecodeInstructions(String byteCodeStartingFromMethodCode) {
        List<String> instructions = new ArrayList<>();

        String[] lines = byteCodeStartingFromMethodCode.split("\n");
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].trim().equals(CODE)) continue;


            if (lines[i].contains(CLASS_END) || lines[i].trim().equals(EMPTY_STRING)) break;
            instructions.add(lines[i].trim().split("[\\s]+")[1]);
        }
        return instructions;
    }
}
