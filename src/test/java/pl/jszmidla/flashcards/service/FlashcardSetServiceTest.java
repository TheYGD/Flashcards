package pl.jszmidla.flashcards.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.jszmidla.flashcards.data.FlashcardSet;
import pl.jszmidla.flashcards.data.User;
import pl.jszmidla.flashcards.data.dto.FlashcardDto;
import pl.jszmidla.flashcards.data.dto.FlashcardSetDto;
import pl.jszmidla.flashcards.data.exception.ForbiddenException;
import pl.jszmidla.flashcards.data.mapper.FlashcardSetMapper;
import pl.jszmidla.flashcards.repository.FlashcardSetRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FlashcardSetServiceTest {

    @Mock
    FlashcardSetRepository flashcardSetRepository;
    @Mock
    FlashcardSetMapper flashcardSetMapper;
    @InjectMocks
    FlashcardSetService flashcardSetService;


    @Test
    void find_by_id() {
        when( flashcardSetRepository.findById(any()) ).thenReturn( Optional.of(new FlashcardSet()) );
        flashcardSetService.find_by_id(1L);
    }

    @Test
    void create_set() {
        FlashcardSetDto flashcardSetDto = create_flashset_dto();
        User author = create_user();
        when( flashcardSetMapper.dto_to_entity(any()) ).thenReturn( new FlashcardSet() );

        Long setId = flashcardSetService.create_set(flashcardSetDto, author);

        assertNull(setId);
    }

    @Test
    void delete_set_success() {
        User user = create_user();
        FlashcardSet flashcardSet = Mockito.spy(FlashcardSet.class);
        when( flashcardSet.getAuthorId() ).thenReturn(user.getId());
        when( flashcardSetRepository.findById(any()) ).thenReturn( Optional.of(flashcardSet) );

        flashcardSetService.delete_set(flashcardSet.getId(), user);
    }

    @Test
    void delete_set_fail() {
        User user = create_user();
        FlashcardSet flashcardSet = Mockito.spy(FlashcardSet.class);
        when( flashcardSet.getAuthorId() ).thenReturn(user.getId() + 1);
        when( flashcardSetRepository.findById(any()) ).thenReturn( Optional.of(flashcardSet) );

        assertThrows( ForbiddenException.class, () -> flashcardSetService.delete_set(flashcardSet.getId(), user) );
    }


    private User create_user() {
        User user = new User();
        user.setId(1L);
        return user;
    }

    private FlashcardSetDto create_flashset_dto() {
        FlashcardSetDto flashcardSetDto = new FlashcardSetDto();
        flashcardSetDto.setName("name");
        flashcardSetDto.setDescription("desc");

        FlashcardDto flashcardDto1 = create_flashcard_dto("front1", "back1");
        FlashcardDto flashcardDto2 = create_flashcard_dto("front2", "back2");
        FlashcardDto flashcardDto3= create_flashcard_dto("front3", "back3");

        flashcardSetDto.setFlashcardList( List.of(flashcardDto1, flashcardDto2, flashcardDto3) );
        return flashcardSetDto;
    }

    private FlashcardDto create_flashcard_dto(String front, String back) {
        FlashcardDto flashcardDto = new FlashcardDto();
        flashcardDto.setFront(front);
        flashcardDto.setBack(back);
        return flashcardDto;
    }
}