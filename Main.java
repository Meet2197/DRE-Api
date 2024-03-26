import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@EnableKafka
public class DataTransferApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataTransferApplication.class, args);
    }

    @RestController
    public static class DataTransferController {

        @Value("${kafka.bootstrap-servers}")
        private String kafkaBootstrapServers;

        @Value("${nextcloud.api.url}")
        private String nextcloudApiUrl;

        private final KafkaTemplate<String, CsvData> kafkaTemplate;

        public DataTransferController(KafkaTemplate<String, CsvData> kafkaTemplate) {
            this.kafkaTemplate = kafkaTemplate;
        }

        @PostMapping("/upload-csv")
        public ResponseEntity<String> uploadCsvToKafka(@RequestBody CsvData csvData) {
            // Produce the CSV data to Kafka topic
            kafkaTemplate.send("csv-topic", csvData);

            // Forward CSV data to Nextcloud API
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.postForEntity(nextcloudApiUrl, csvData, String.class);

            return ResponseEntity.ok("CSV data uploaded successfully!");
        }
    }

    public static class CsvData {
        // Define your CSV data structure here
        private String data;

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }

    // Kafka configuration
    // You can define Kafka properties in application.properties or application.yml
    @Configuration
    public static class KafkaConfig {

        @Value("${kafka.bootstrap-servers}")
        private String bootstrapServers;

        @Bean
        public ProducerFactory<String, CsvData> producerFactory() {
            Map<String, Object> configProps = new HashMap<>();
            configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
            configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
            return new DefaultKafkaProducerFactory<>(configProps);
        }

        @Bean
        public KafkaTemplate<String, CsvData> kafkaTemplate() {
            return new KafkaTemplate<>(producerFactory());
        }
    }
}