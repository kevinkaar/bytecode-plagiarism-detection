import helper.ComparableHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import plagiarismdetection.PlagiarismDetector;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws IOException {
        //PlagiarismDetector plagiarismDetector = new PlagiarismDetector(new ComparableHelper("KLASSID1", "filestotest/TestIdentical1.java"), new ComparableHelper("KLASSID2", "filestotest/TestIdentical2.java"));
        //PlagiarismDetector plagiarismDetector = new PlagiarismDetector(new ComparableHelper("klass_ID1", "filestotest/TestChangeOfOrder1.java"), new ComparableHelper("klass_ID2", "filestotest/TestChangeOfOrder2.java"));
        //PlagiarismDetector plagiarismDetector = new PlagiarismDetector(new ComparableHelper("klass_ID1", "filestotest/TestArbitraryCode1.java"), new ComparableHelper("klass_ID2", "filestotest/TestArbitraryCode2.java"));
        //PlagiarismDetector plagiarismDetector = new PlagiarismDetector(new ComparableHelper("klass_ID1", "filestotest/TrainStation_moss84.class"), new ComparableHelper("klass_ID2", "filestotest/Station_moss84.java"));
        //PlagiarismDetector plagiarismDetector = new PlagiarismDetector(new ComparableHelper("klass_ID1", "filestotest/train_moss77_a.class"), new ComparableHelper("klass_ID2", "filestotest/Train_moss77_b.class"));
        PlagiarismDetector plagiarismDetector = new PlagiarismDetector(new ComparableHelper("klass_ID1", args[0]), new ComparableHelper("klass_ID2", args[1]));


        try {
            plagiarismDetector.compareBytecodes();
        } catch (InterruptedException | TimeoutException e) {
            log.error("Error comparing bytecodes", e);
        }
    }
}
