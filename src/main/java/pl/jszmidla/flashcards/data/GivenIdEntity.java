package pl.jszmidla.flashcards.data;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Getter
@Setter
public class GivenIdEntity {

    @Id
    private Long id;
}
