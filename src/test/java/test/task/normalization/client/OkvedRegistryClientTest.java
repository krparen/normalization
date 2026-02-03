package test.task.normalization.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import test.task.normalization.dto.FlatOkvedDto;
import test.task.normalization.dto.OkvedDto;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OkvedRegistryClientTest {

    private static final String MOCK_GITHUB_URL = "https://definitely-github.com/file-with-okveds.json";

    private RestTemplate mockRestTemplate = Mockito.mock(RestTemplate.class);
    private OkvedRegistryClient okvedRegistryClient;

    @BeforeEach
    public void initEach() {
        okvedRegistryClient = new OkvedRegistryClient(mockRestTemplate, new ObjectMapper());
        ReflectionTestUtils.setField(okvedRegistryClient, "fileUrl", MOCK_GITHUB_URL);
    }

    @Test
    void getOkvedsFromGithub() throws JsonProcessingException {
        List<OkvedDto> responseOkveds = new ArrayList<>();

        OkvedDto childOkved = new OkvedDto();
        childOkved.setCode("88.8");
        childOkved.setName("Три восьмёрки");

        OkvedDto parentOkved = new OkvedDto();
        parentOkved.setCode("88");
        parentOkved.setName("Восемь восемь");
        parentOkved.setItems(List.of(childOkved));

        responseOkveds.add(parentOkved);

        String responseAsString = new ObjectMapper().writeValueAsString(responseOkveds);
        ResponseEntity<String> responseEntity = ResponseEntity.ok(responseAsString);

        when(mockRestTemplate.getForEntity(MOCK_GITHUB_URL, String.class)).thenReturn(responseEntity);

        List<FlatOkvedDto> result = okvedRegistryClient.getOkvedsFromGithub();
        assertEquals(2, result.size());

        FlatOkvedDto flatParent = result.stream()
                .filter(o -> o.getCode().equals(parentOkved.getCode()))
                .findFirst()
                .orElseThrow();

        assertThat(flatParent).usingRecursiveComparison().
                comparingOnlyFields("code", "name")
                .isEqualTo(parentOkved);

        FlatOkvedDto flatChild = result.stream()
                .filter(o -> o.getCode().equals(childOkved.getCode()))
                .findFirst()
                .orElseThrow();

        assertThat(flatChild).usingRecursiveComparison().
                comparingOnlyFields("code", "name")
                .isEqualTo(childOkved);
    }
}