package plagiarismdetection.bytecodereader;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.stream.slf4j.Slf4jStream;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class BytecodeReader {

    public String getBytecode(String path) throws InterruptedException, TimeoutException, IOException {

        if (FilenameUtils.getExtension(path).equals("java")) {
            compileJavaFileToClassFile(path);
            path = changePathExtensionToClass(path);

            return getClassBytecode(path);
        }
        return getClassBytecode(path);
    }

    private String changePathExtensionToClass(String path) {
        return FilenameUtils.getFullPath(path) + FilenameUtils.getBaseName(path) + ".class";
    }

    private String getClassBytecode(String path) throws InterruptedException, TimeoutException, IOException {
        String[] javapCommand = getProcessExecutorCommandByOSSystemAndProcessName(CommandLineProcess.JAVAP, path);

        return new ProcessExecutor()
                .command(javapCommand)
                .readOutput(true)
                .redirectOutput(Slf4jStream.of(LoggerFactory.getLogger(getClass().getName() + ".SysJavapProcess")).asInfo())
                .execute()
                .getOutput()
                .getUTF8()
                .trim();
    }

    private void compileJavaFileToClassFile(String path) throws InterruptedException, TimeoutException, IOException {
        String[] javacCommand = getProcessExecutorCommandByOSSystemAndProcessName(CommandLineProcess.JAVAC, path);

        System.out.println(
                new ProcessExecutor()
                        .command(javacCommand)
                        .readOutput(true)
                        .redirectOutput(Slf4jStream.of(LoggerFactory.getLogger(getClass().getName() + ".SysJavacProcess")).asError())
                        .execute()
                        .getOutput()
                        .getUTF8()
                        .trim());
    }

    private String[] getProcessExecutorCommandByOSSystemAndProcessName(CommandLineProcess process, String path) {
        String[] command = new String[]{};

        if (process.equals(CommandLineProcess.JAVAC)) {
            command = new String[]{"javac", path};
        } else if (process.equals(CommandLineProcess.JAVAP)) {
            command = new String[]{"javap", "-c", "-p", path};
        }

        return command;
    }

}
