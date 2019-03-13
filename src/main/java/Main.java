import java.io.IOException;
import org.apache.commons.cli.*;

public class Main {

    public static void main(String[] args) {

        int portNumber;
        String fileDirectory;

        Options options = new Options();

        Option portOption = new Option("p", "port", true, "port to listen on, default is 5000");
        portOption.setRequired(false);
        options.addOption(portOption);

        Option fileDirectoryOption = new Option("d", "directory", true, "directory to serve files from, default is the PUBLIC_DIR variable");
        fileDirectoryOption.setRequired(false);
        options.addOption(fileDirectoryOption);


        Server server = new Server();
        portNumber = 5000;
        fileDirectory = "/Users/anton.briganti/Documents/titan cat";


        System.out.println("starting server");
        server.startServer(portNumber, fileDirectory);

    }

}