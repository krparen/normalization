package test.task.normalization.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import test.task.normalization.dto.OkvedDto;
import test.task.normalization.dto.FlatOkvedDto;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@Getter
@Setter
@RequiredArgsConstructor
public class OkvedRegistryClient {

    private final RestTemplate restTemplate;

    @Value("${okved.file.url}")
    private String fileUrl;


    private ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    public List<FlatOkvedDto> getOkvedsFromGithub() {
        ResponseEntity<String> fileAsResponse = restTemplate.getForEntity(fileUrl, String.class);

        String fileAsString = fileAsResponse.getBody();

        CollectionType typeReference =
                TypeFactory.defaultInstance().constructCollectionType(List.class, OkvedDto.class);
        List<OkvedDto> okvedsFromFile = objectMapper.readValue(fileAsString, typeReference);
        log.debug("parsed okveds from file: {}", okvedsFromFile);

        List<FlatOkvedDto> result = new ArrayList<>();
        okvedsFromFile.forEach(okved -> addFlatOkveds(okved, result));
        log.debug("okved storage: {}", result);

        return result;
    }

    private void addFlatOkveds(OkvedDto dto, List<FlatOkvedDto> storage) {
        if (dto == null) {
            return;
        }
        if (dto.getCode() == null) {
            log.warn("Okved with name {} has null code, it cannot be matched with phone number and will be skipped", dto.getName());
            return;
        }

        FlatOkvedDto flatOkvedDto = new FlatOkvedDto();
        flatOkvedDto.setName(dto.getName());
        flatOkvedDto.setCode(dto.getCode());
        flatOkvedDto.setCodeNoDots(dto.getCode().replace(".", ""));
        storage.add(flatOkvedDto);

        if (dto.getItems() != null && !dto.getItems().isEmpty()) {
            dto.getItems().forEach(item -> addFlatOkveds(item, storage));
        }
    }
}
