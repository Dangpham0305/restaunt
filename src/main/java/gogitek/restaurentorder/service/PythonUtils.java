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
    private static final String currentDirectory = System.getProperty("user.dir");
    private static final Path path = Paths.get(currentDirectory + Paths.get("/target/classes/static/python"));

    public String recognize(String image) throws IOException {
        String pythonPath = path.toAbsolutePath() + "/face_recognize.py";
        System.out.println(pythonPath);
        String command = "python " +pythonPath;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CommandLine commandline = CommandLine.parse(command);
        commandline.addArgument(image);
        return exec(command, outputStream, commandline);
    }
    public String update() throws IOException {
        String pythonPath = path.toAbsolutePath() + "/update_faces.py";
        String command = "python " +pythonPath;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CommandLine commandline = CommandLine.parse(command);
        return exec(command, outputStream, commandline);
    }

    private String exec(String command, ByteArrayOutputStream outputStream, CommandLine commandline) {
        DefaultExecutor exec = new DefaultExecutor();
        exec.setStreamHandler(new PumpStreamHandler(outputStream));
        int exitCode;

        try {
            exitCode = exec.execute(commandline);
        } catch (IOException e) {
            throw new RuntimeException("Unable to execute " + command + ": " + outputStream, e);
        }
        if (exitCode != 0) {
            throw new RuntimeException(command + " exited with code " + exitCode + ", " + outputStream);
        }
        return outputStream.toString();
    }
}

