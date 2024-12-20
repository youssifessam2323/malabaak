package io.joework.malabaakapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
public class TestUtils {

    public static ObjectMapper getMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return mapper;
    }

    public static <T> T jsonParserByFile(JsonTypes fileName, String payload, Class<T> model) {
        ObjectMapper objectMapper = TestUtils.getMapper();
        try {
            File file = new File("src/test/resources/test/" + fileName.getFileName() + "/" + payload + ".json");
            return objectMapper.readValue(file, model);
        } catch (IOException e) {
            log.info("Failed to load JSON file, Please ensure format is correct and file exists");
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> jsonParserForListByFile(JsonTypes fileName, String payload, Class<T> model) {
        ObjectMapper objectMapper = TestUtils.getMapper();
        try {
            File file = new File("src/test/resources/test/"+ fileName.getFileName() + "/" + payload +".json");
            return objectMapper.readValue(file, objectMapper.getTypeFactory().constructCollectionType(List.class, model));
        } catch (IOException e) {
            log.info("Failed to load JSON file, Please ensure format is correct and file exists");
            throw new RuntimeException(e);
        }
    }

    public static String asJsonString(final Object obj) {
        try {
            return getMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
