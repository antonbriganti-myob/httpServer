import java.io.IOException;
import org.apache.commons.cli.*;

public class Main {

    public static void main(String[] args) {

        int defaultPort = 5000;
        String defaultDirectory = "/Users/anton.briganti/Documents/Development/httpServerApplication/cob_spec/public";


        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;

        Options options = createCLIOptions();


        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            return;
        }

        int portNumber = cmd.getOptionValue("port") != null ? Integer.valueOf(cmd.getOptionValue("port")) : defaultPort;
        String fileDirectory = cmd.getOptionValue("directory") != null ? cmd.getOptionValue("directory") : defaultDirectory;
        Server server = new Server();



        System.out.println("Attempting to start server");
        System.out.println("Port Number: " + portNumber);
        System.out.println("Base directory: " + fileDirectory);


        try {
            server.startServer(portNumber, fileDirectory);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static Options createCLIOptions() {
        Options options = new Options();
        Option portOption = new Option("p", "port", true, "port to listen on, default is 5000");
        portOption.setRequired(false);
        options.addOption(portOption);

        Option fileDirectoryOption = new Option("d", "directory", true, "directory to serve files from, default is the PUBLIC_DIR variable");
        fileDirectoryOption.setRequired(false);
        options.addOption(fileDirectoryOption);
        return options;
    }

}