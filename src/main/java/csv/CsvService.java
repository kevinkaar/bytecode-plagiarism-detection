package csv;

import classbytecode.MappedByteCode;
import classbytecode.MappedByteCodeMethod;
import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class CsvService {

    private static final Logger log = LoggerFactory.getLogger(CsvService.class);

    private static final String FILE_NAME = "bytecode.csv";

    private static final String CSV_HEADER_CLASS_ID = "classId";

    private static final String CSV_HEADER_BYTECODE = "bytecode";

    private static final String CSV_HEADER_METHOD = "method";

    private static final char CSV_DELIMITER = ',';

    public static void writeOrAppendBytecodeToCSV(MappedByteCode... mappedBytecode) {
        boolean fileExists = new File(FILE_NAME).exists();

        CsvWriter csvOutput = null;
        try {
            csvOutput = new CsvWriter(new FileWriter(FILE_NAME, true), CSV_DELIMITER);

            //If file doesn't exist, write headers first
            if (!fileExists) {
                csvOutput.write(CSV_HEADER_CLASS_ID);
                csvOutput.write(CSV_HEADER_METHOD);
                csvOutput.write(CSV_HEADER_BYTECODE);
                csvOutput.endRecord();
            }

            for (MappedByteCode mbc : mappedBytecode) {
                for (MappedByteCodeMethod method : mbc.getMethods()) {
                    csvOutput.write(String.valueOf(mbc.getClassId()));
                    csvOutput.write(method.getMethodName());
                    write(csvOutput, String.join(" ", method.getBytecode()));

                    csvOutput.endRecord();
                }
            }

            csvOutput.close();

        } catch (IOException e) {
            log.error("Error writing method bytecode to csv", e);
        } finally {
            csvOutput.close();
        }
    }

    private static void write(CsvWriter writer, String value) throws IOException {
        writer.write(value);
    }

}
