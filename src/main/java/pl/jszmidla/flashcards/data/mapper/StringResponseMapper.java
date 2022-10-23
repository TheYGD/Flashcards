package pl.jszmidla.flashcards.data.mapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import pl.jszmidla.flashcards.data.dto.StringResponse;

@Component
public class StringResponseMapper {

    public ResponseEntity<String> toResponseEntity(StringResponse stringResponse) {
        return ResponseEntity.status( HttpStatus.valueOf(stringResponse.getStatusCode()) )
                .body( stringResponse.getBody() );
    }
}
