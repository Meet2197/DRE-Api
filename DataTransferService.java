import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DataTransferService {

    @Autowired
    private KafkaTemplate<String, CsvData> kafkaTemplate;

    @Value("${nextcloud.api.url}")
    private String nextcloudApiUrl;

    public void uploadCsvData(CsvData csvData) {
        // Produce the CSV data to Kafka topic
        kafkaTemplate.send("csv-topic", csvData);

        // Forward CSV data to Nextcloud API
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForEntity(nextcloudApiUrl, csvData, String.class);
    }
}
