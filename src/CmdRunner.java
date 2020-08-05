import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class CmdRunner implements IPing{
    public CmdRunner() {

    }

    public String cmdPing(String address) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        StringBuilder output = new StringBuilder();
        processBuilder.command("/bin/sh", "-c", String.format("ping %s -c 1 -t 1", address));

        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            while((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                //TODO debugger: unreachable host logging
                return null;
            }

        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
        return address;
    }
}
