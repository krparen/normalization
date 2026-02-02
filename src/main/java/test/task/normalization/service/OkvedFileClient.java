package test.task.normalization.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import test.task.normalization.dto.OkvedDto;

import java.util.List;

@Component
@Getter
@Setter
public class OkvedFileClient {

    @Value("okved.file.url")
    private String fileUrl;


    private ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    void downloadFile() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> fileAsResponse = restTemplate.getForEntity(fileUrl, String.class);

        String fileAsString = fileAsResponse.getBody();
        System.out.println("file: \n");
        System.out.println(fileAsString);

        CollectionType typeReference =
                TypeFactory.defaultInstance().constructCollectionType(List.class, OkvedDto.class);
        List<OkvedDto> okvedsFromFile = objectMapper.readValue(fileAsString, typeReference);
        System.out.println("dto: \n");
        System.out.println(okvedsFromFile);
    }
}
