package pl.jszmidla.flashcards;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectToJsonMapper {

    public String toJson(Object flashcardResponseList) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(flashcardResponseList);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
