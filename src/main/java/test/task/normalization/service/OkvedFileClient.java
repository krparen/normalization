package test.task.normalization.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import test.task.normalization.dto.OkvedDto;
import test.task.normalization.dto.ReducedOkvedDto;

import java.util.ArrayList;
import java.util.List;

@Component
@Getter
@Setter
public class OkvedFileClient {

    @Value("okved.file.url")
    private String fileUrl;


    private ObjectMapper objectMapper = new ObjectMapper();

    private List<ReducedOkvedDto> okvedStorage = new ArrayList<>();

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

        okvedsFromFile.forEach(okved -> addReducedOkved(okved, okvedStorage));

        System.out.println("storage: \n");
        System.out.println(okvedStorage);
    }

    private void addReducedOkved(OkvedDto dto, List<ReducedOkvedDto> storage) {
        if (dto == null) {
            return;
        }

        ReducedOkvedDto reducedOkvedDto = new ReducedOkvedDto();
        reducedOkvedDto.setName(dto.getName());
        reducedOkvedDto.setCode(dto.getCode());
        storage.add(reducedOkvedDto);

        if (dto.getItems() != null && !dto.getItems().isEmpty()) {
            dto.getItems().forEach(item -> addReducedOkved(item, storage));
        }
    }
}
