package test.task.normalization.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class OkvedFileClient {

    @Getter
    @Setter
    @Value("okved.file.url")
    private String fileUrl;

    void downloadFile() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> fileAsResponse = restTemplate.getForEntity(fileUrl, String.class);

        String fileAsString = fileAsResponse.getBody();
        System.out.println("file: \n");
        System.out.println(fileAsString);
    }
}
