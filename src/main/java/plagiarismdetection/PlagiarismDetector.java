package plagiarismdetection;

import classbytecode.MappedByteCode;
import classbytecode.MappedByteCodeMethod;
import csv.CsvService;
import helper.ComparableHelper;
import helper.ClassComparisonResult;
import helper.MethodComparisonResult;
import plagiarismdetection.bytecodereader.BytecodeMapper;
import plagiarismdetection.bytecodereader.BytecodeReader;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class PlagiarismDetector {

    private ComparableHelper comparable1;

    private ComparableHelper comparable2;

    public PlagiarismDetector(ComparableHelper comparable1, ComparableHelper comparable2) {
        this.comparable1 = comparable1;
        this.comparable2 = comparable2;
    }

    public void compareBytecodes() throws InterruptedException, IOException, TimeoutException {
        BytecodeReader reader = new BytecodeReader();
        BytecodeMapper mapper = new BytecodeMapper();

        String bytecode1 = reader.getBytecode(comparable1.getPath());
        String bytecode2 = reader.getBytecode(comparable2.getPath());

        String className1 = mapper.getClassNameFromBytecode(bytecode1);
        String className2 = mapper.getClassNameFromBytecode(bytecode2);
        List<MappedByteCodeMethod> methods1 = mapper.getMethodsFromByteCode(bytecode1, className1);
        List<MappedByteCodeMethod> methods2 = mapper.getMethodsFromByteCode(bytecode2, className2);
        MappedByteCode mappedByteCode1 = new MappedByteCode(comparable1.getId(), className1, methods1);
        MappedByteCode mappedByteCode2 = new MappedByteCode(comparable2.getId(), className2, methods2);

        System.out.println("### EXPORT TO CSV START ###");
        CsvService.writeOrAppendBytecodeToCSV(mappedByteCode1, mappedByteCode2);
        System.out.println("### EXPORT TO CSV END   ###");

        System.out.println("### FIND PLAGIARISM START ###\n");
        ClassComparisonResult results = getAllToAllComparedMethodsComparedResults(mappedByteCode1, mappedByteCode2);

        mappedByteCode1.getMethods().stream().forEach(m -> Collections.sort(m.getBytecode()));
        mappedByteCode2.getMethods().stream().forEach(m -> Collections.sort(m.getBytecode()));
        ClassComparisonResult resultsSorted = getAllToAllComparedMethodsComparedResults(mappedByteCode1, mappedByteCode2);

        System.out.println(String.format("Comparing classes with ID's: %s and %s", results.getClassId1(), results.getClassId2()));
        results.getMethodComparisonResult().stream()
                .forEach(res -> System.out.println(String.format("Methods (%s) and (%s) instructions match %d/%d - Shorter %s in longer",
                        res.getMethod1(), res.getMethod2(), res.getMatchingInstructions(), res.getTotalInstructions(),
                        new DecimalFormat("#.##").format(res.getShorterInLongerRatio()) + "%")));

        resultsSorted.getMethodComparisonResult().stream()
                .forEach(res -> System.out.println(String.format("Methods (%s) and (%s) SORTED instructions match %d/%d - Shorter %s in longer",
                        res.getMethod1(), res.getMethod2(), res.getMatchingInstructions(), res.getTotalInstructions(),
                        new DecimalFormat("#.##").format(res.getShorterInLongerRatio()) + "%")));
        System.out.println("\n### FIND PLAGIARISM END   ###");
    }

    private ClassComparisonResult getAllToAllComparedMethodsComparedResults(MappedByteCode toCompare1, MappedByteCode toCompare2) {
        ClassComparisonResult result = new ClassComparisonResult(toCompare1.getClassId(), toCompare2.getClassId());
        List<MappedByteCodeMethod> methods1 = toCompare1.getMethods();
        List<MappedByteCodeMethod> methods2 = toCompare2.getMethods();

        List<MethodComparisonResult> methodComparisonResults = new ArrayList<>();
        for (int i = 0; i < methods1.size(); i++) {
            for (int j = 0; j < methods2.size(); j++) {
                methodComparisonResults.add(getMethodComparisonResult(methods1.get(i), methods2.get(j)));
            }
        }
        result.setMethodComparisonResult(methodComparisonResults);
        return result;
    }

    private MethodComparisonResult getMethodComparisonResult(MappedByteCodeMethod toCompare1, MappedByteCodeMethod toCompare2) {
        MethodComparisonResult result = new MethodComparisonResult(toCompare1.getMethodName(), toCompare2.getMethodName());
        List<String> bytecode1 = toCompare1.getBytecode();
        List<String> bytecode2 = toCompare2.getBytecode();

        result.setTotalInstructions(getTotalInstructionsCount(bytecode1, bytecode2));
        getMatchingInstructionsCountAndShorterInLongerRatio(bytecode1, bytecode2, result);

        return result;
    }

    private MethodComparisonResult getMatchingInstructionsCountAndShorterInLongerRatio(List<String> bytecode1,
                                                                                       List<String> bytecode2,
                                                                                       MethodComparisonResult result) {
        if (bytecode1.size() < bytecode2.size()) {
            long matchingInstructions = findMatchingSequentialInstructionsCount(bytecode1, bytecode2);
            result.setMatchingInstructions(matchingInstructions);
            result.setShorterInLongerRatio((double)matchingInstructions / bytecode1.size() * 100);
            return result;
        } else {
            long matchingInstructions = findMatchingSequentialInstructionsCount(bytecode2, bytecode1);
            result.setMatchingInstructions(matchingInstructions);
            result.setShorterInLongerRatio((double)matchingInstructions / bytecode2.size() * 100);
            return result;
        }
    }

    private long findMatchingSequentialInstructionsCount(List<String> bytecodeWithLessInstructions,
                                                         List<String> bytecodeWithMostInstructions) {
        long maxCount = 0L;
        long current;

        for (int i = 0; i < bytecodeWithMostInstructions.size(); i++) {
            String currentMostIns = bytecodeWithMostInstructions.get(i).trim();
            for (int j = 0; j < bytecodeWithLessInstructions.size(); j++) {
                String currentLessIns = bytecodeWithLessInstructions.get(j).trim();

                if (currentMostIns.equals(currentLessIns)) {
                    current = getSequentialSublistCount(
                            bytecodeWithMostInstructions.subList(i + 1, bytecodeWithMostInstructions.size()),
                            bytecodeWithLessInstructions.subList(j + 1, bytecodeWithLessInstructions.size()));
                    if (current > maxCount) {
                        maxCount = current;
                    }
                }
            }
        }


        return maxCount;
    }

    private long getSequentialSublistCount(List<String> bytecodeSublist1, List<String> bytecodeSublist2) {
        long counter = 1L;

        for (int i = 0; i < bytecodeSublist1.size() && i < bytecodeSublist2.size(); i++) {
            if (bytecodeSublist1.get(i).equals(bytecodeSublist2.get(i))) {
                counter++;
            }
        }
        return counter;
    }

    private long getTotalInstructionsCount(List<String> bytecode1, List<String> bytecode2) {
        if (bytecode1.size() > bytecode2.size()) {
            return bytecode1.size();
        } else {
            return bytecode2.size();
        }
    }
}
