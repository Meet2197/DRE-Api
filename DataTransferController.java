import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataTransferController {

    @Autowired
    private DataTransferService dataTransferService;

    @PostMapping("/upload-csv")
    public ResponseEntity<String> uploadCsvToKafka(@RequestBody CsvData csvData) {
        dataTransferService.uploadCsvData(csvData);
        return ResponseEntity.ok("CSV data uploaded successfully!");
    }
}
