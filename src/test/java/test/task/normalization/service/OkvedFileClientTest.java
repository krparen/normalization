package test.task.normalization.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OkvedFileClientTest {

    @Test
    void downloadFile() {
        var client = new OkvedFileClient();
        client.setFileUrl("https://raw.githubusercontent.com/bergstar/testcase/master/okved.json");
        client.setObjectMapper(new ObjectMapper());
        client.downloadFile();
    }
}