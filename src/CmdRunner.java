import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;

public class CmdRunner implements ICmd {
    ProcessBuilder process;
    public CmdRunner() {
        this.process = new ProcessBuilder();
    }

    public String cmdPing(String address) {
        if (this.Run("ping %s -c 1 -t 1", address) == null)
            return null;
        return address;
    }

    public String cmdNmap(String address) {
        return this.Run("nmap", address);
    }

    private String Run(String command, String address) {
        // returns unformatted output of command
        StringBuilder output = new StringBuilder();
        this.process.command("/bin/sh", "-c", String.format("%s %s", command, address));

        try {
            Process process = this.process.start();
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
        return output.toString();
    }
}
