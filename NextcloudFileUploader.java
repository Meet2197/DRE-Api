import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

public class NextcloudFileUploader {

    private static final String NEXTCLOUD_URL = "https://your-nextcloud-domain.com/remote.php/dav/files/"; // Specify your Nextcloud URL
    private static final String USERNAME = "your-username"; // Specify your Nextcloud username
    private static final String PASSWORD = "your-password"; // Specify your Nextcloud password

    public static void uploadCsvFileToNextcloud(File csvFile, String destinationFolder) throws IOException {
        String webDavUrl = NEXTCLOUD_URL + USERNAME + "/" + destinationFolder + "/" + csvFile.getName();
        URL url = new URL(webDavUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("PUT");

        // Set up Basic Authentication
        String auth = USERNAME + ":" + PASSWORD;
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes());
        String authHeaderValue = "Basic " + new String(encodedAuth);
        connection.setRequestProperty("Authorization", authHeaderValue);

        // Set up content type
        connection.setRequestProperty("Content-Type", "text/csv");

        // Set up content length
        connection.setRequestProperty("Content-Length", String.valueOf(csvFile.length()));

        // Enable output and write file content to the connection
        connection.setDoOutput(true);
        try (InputStream inputStream = new FileInputStream(csvFile)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                connection.getOutputStream().write(buffer, 0, bytesRead);
            }
        }

        // Execute the request
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_CREATED) {
            System.out.println("CSV file uploaded successfully to Nextcloud.");
        } else {
            System.err.println("Failed to upload CSV file to Nextcloud. Response code: " + responseCode);
        }
    }

    public static void main(String[] args) {
        try {
            File csvFile = new File("path/to/your/csv/file.csv");
            String destinationFolder = "uploads"; // Specify the destination folder in Nextcloud
            uploadCsvFileToNextcloud(csvFile, destinationFolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
