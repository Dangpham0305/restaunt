package gogitek.restaurentorder.service;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class PythonUtils {
    private static final String DIRECTORY = System.getProperty("user.dir");
    private static final Path path = Paths.get(DIRECTORY + Paths.get("/target/classes/static/image/FacialPhoto"));

    public String pythonExec(String image) throws IOException {

        String line = "AcroRd32.exe /p /h " + path;
        CommandLine cmdLine = CommandLine.parse(line);
        cmdLine.addArgument("/p");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
        DefaultExecutor executor = new DefaultExecutor();
        executor.setStreamHandler(streamHandler);
        Integer exitValue = executor.execute(cmdLine);
        return exitValue.toString();
    }
}
