import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class NextcloudFileUploader {

    private static final String NEXTCLOUD_URL = "https://your-nextcloud-domain.com/remote.php/dav/files/"; // Specify your Nextcloud URL
    private static final String USERNAME = "your-username"; // Specify your Nextcloud username
    private static final String PASSWORD = "your-password"; // Specify your Nextcloud password

    public static void uploadCsvFileToNextcloud(File csvFile, String destinationFolder) throws IOException {
        String webDavUrl = NEXTCLOUD_URL + USERNAME + "/" + destinationFolder + "/" + csvFile.getName();

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPut httpPut = new HttpPut(webDavUrl);
            httpPut.setEntity(getFileEntity(csvFile));
            httpPut.addHeader("Content-Type", "text/csv");

            // Set up Basic Authentication
            httpClient.getCredentialsProvider().setCredentials(
                    new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT),
                    new UsernamePasswordCredentials(USERNAME, PASSWORD));

            // Execute the request
            HttpResponse response = httpClient.execute(httpPut);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 201) {
                System.out.println("CSV file uploaded successfully to Nextcloud.");
            } else {
                System.err.println("Failed to upload CSV file to Nextcloud. Status code: " + statusCode);
            }
        }
    }

    private static HttpEntity getFileEntity(File csvFile) throws IOException {
        try (InputStream inputStream = new FileInputStream(csvFile)) {
            byte[] buffer = new byte[(int) csvFile.length()];
            int bytesRead = inputStream.read(buffer);
            if (bytesRead != -1) {
                return new ByteArrayEntity(buffer);
            }
        }
        throw new IOException("Failed to read CSV file.");
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
