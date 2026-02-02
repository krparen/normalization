package test.task.normalization.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

class OkvedRegistryClientTest {

    @Test
    void downloadFile() {
        var client = new OkvedRegistryClient(new RestTemplate());
        client.setFileUrl("https://raw.githubusercontent.com/bergstar/testcase/master/okved.json");
        client.setObjectMapper(new ObjectMapper());
        client.getOkvedsFromGithub();
    }
}