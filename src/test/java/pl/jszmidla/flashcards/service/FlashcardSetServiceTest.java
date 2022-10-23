package pl.jszmidla.flashcards.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import pl.jszmidla.flashcards.data.Flashcard;
import pl.jszmidla.flashcards.data.FlashcardSet;
import pl.jszmidla.flashcards.data.User;
import pl.jszmidla.flashcards.data.dto.StringResponse;
import pl.jszmidla.flashcards.data.dto.flashcard.FlashcardRequest;
import pl.jszmidla.flashcards.data.dto.flashcard.FlashcardResponse;
import pl.jszmidla.flashcards.data.dto.flashcard.FlashcardSetRequest;
import pl.jszmidla.flashcards.data.dto.flashcard.FlashcardSetResponse;
import pl.jszmidla.flashcards.data.dto.flashcard.edit.FlashcardChangeRequest;
import pl.jszmidla.flashcards.data.dto.flashcard.edit.FlashcardSetChangeRequest;
import pl.jszmidla.flashcards.data.dto.flashcard.edit.FlashcardSetChangeResponse;
import pl.jszmidla.flashcards.data.exception.ForbiddenException;
import pl.jszmidla.flashcards.data.mapper.FlashcardMapper;
import pl.jszmidla.flashcards.data.mapper.FlashcardSetMapper;
import pl.jszmidla.flashcards.repository.FlashcardRepository;
import pl.jszmidla.flashcards.repository.FlashcardSetRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlashcardSetServiceTest {

    @Mock
    FlashcardRepository flashcardRepository;
    @Mock
    FlashcardSetRepository flashcardSetRepository;
    FlashcardMapper flashcardMapper = new FlashcardMapper();
    FlashcardSetMapper flashcardSetMapper = new FlashcardSetMapper(flashcardMapper);
    @Mock
    UsersActiveSetService usersActiveSetService;
    @Mock
    UsersRecentSetService usersRecentSetService;
    @InjectMocks
    FlashcardSetService flashcardSetService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        flashcardSetService = new FlashcardSetService(flashcardSetRepository, flashcardRepository, flashcardSetMapper,
                flashcardMapper);
    }


    @Test
    void findById() {
        when( flashcardSetRepository.findById(any()) ).thenReturn( Optional.of(new FlashcardSet()) );

        flashcardSetService.findById(1L);
    }

    @Test
    void createSet() {
        FlashcardSetRequest flashcardSetRequest = createFlashcardSetDto();
        List<Flashcard> flashcardList = createFlashcardList();
        FlashcardSet flashcardSet = createFlashcardSet(flashcardList);
        User author = flashcardSet.getAuthor();

        Long setId = flashcardSetService.createSet(flashcardSetRequest, author);

        assertNull(setId);
    }

    private List<Flashcard> createFlashcardList() {
        Flashcard flashcard1 = new Flashcard();
        flashcard1.setId(1L);
        Flashcard flashcard2 = new Flashcard();
        flashcard1.setId(1L);
        Flashcard flashcard3 = new Flashcard();
        flashcard1.setId(1L);

        return List.of(flashcard1, flashcard2, flashcard3);
    }

    @Test
    void deleteSetSuccess() {
        String message = "Set deleted.";
        int status = HttpStatus.OK.value();
        User user = createUser(1);
        FlashcardSet flashcardSet = spy(FlashcardSet.class);
        when( flashcardSet.getAuthor() ).thenReturn( user );
        when( flashcardSetRepository.findById(any()) ).thenReturn( Optional.of(flashcardSet) );

        StringResponse response = flashcardSetService.deleteSet(flashcardSet.getId(), user);

        assertEquals( status, response.getStatusCode() );
        assertEquals( message, response.getBody() );
    }

    @Test
    void deleteSetFail() {
        String message = "Error. Try again later.";
        int status = HttpStatus.BAD_REQUEST.value();
        User user = createUser(1);
        User otherUser = createUser(2);
        FlashcardSet flashcardSet = spy(FlashcardSet.class);
        when( flashcardSet.getAuthor() ).thenReturn( otherUser );
        when( flashcardSetRepository.findById(any()) ).thenReturn( Optional.of(flashcardSet) );

        StringResponse response = flashcardSetService.deleteSet(flashcardSet.getId(), user);

        assertEquals( status, response.getStatusCode() );
        assertEquals( message, response.getBody() );
    }

    @Test
    void findSetsByQuery() {
        String query = "someQuery";
        FlashcardSet flashcardSet = createFlashcardSet(List.of());
        when( flashcardSetRepository.findAllByNameContainingIgnoreCase(any()) ).thenReturn( List.of(flashcardSet) );

        flashcardSetService.findSetsByQuery(query);
    }


    private User createUser(long id) {
        User user = new User();
        user.setId(id);
        return user;
    }

    private FlashcardSetRequest createFlashcardSetDto() {
        FlashcardSetRequest flashcardSetRequest = new FlashcardSetRequest();
        flashcardSetRequest.setName("name");
        flashcardSetRequest.setDescription("desc");

        FlashcardRequest flashcardRequest1 = createFlashcardDto("front1", "back1");
        FlashcardRequest flashcardRequest2 = createFlashcardDto("front2", "back2");
        FlashcardRequest flashcardRequest3 = createFlashcardDto("front3", "back3");

        flashcardSetRequest.setFlashcardList( List.of(flashcardRequest1, flashcardRequest2, flashcardRequest3) );
        return flashcardSetRequest;
    }

    private FlashcardRequest createFlashcardDto(String front, String back) {
        FlashcardRequest flashcardRequest = new FlashcardRequest();
        flashcardRequest.setFront(front);
        flashcardRequest.setBack(back);
        return flashcardRequest;
    }

    @Test
    void showSetToUser() {
        FlashcardSet flashcardSet = createFlashcardSet(List.of());
        when( flashcardSetRepository.findById(any()) ).thenReturn(Optional.of(flashcardSet));

        FlashcardSetResponse flashcardSetResponse = flashcardSetService.showSetToUser(1L);

        assertEquals(flashcardSet.getId(), flashcardSetResponse.getId());
        assertEquals(flashcardSet.getName(), flashcardSetResponse.getName());
        assertEquals(flashcardSet.getDescription(), flashcardSetResponse.getDescription());
        assertEquals(flashcardSet.getAuthor().getId(), flashcardSetResponse.getAuthorId());
        assertEquals(flashcardSet.getAuthor().getUsername(), flashcardSetResponse.getAuthorName());
    }

    private FlashcardSet createFlashcardSet(List<Flashcard> flashcardList) {
        User author = createUser(1);
        author.setUsername("arturitto");

        FlashcardSet flashcardSet = new FlashcardSet();
        flashcardSet.setName("name");
        flashcardSet.setDescription("desc");
        flashcardSet.setAuthor(author);
        flashcardSet.setFlashcards(flashcardList);

        return flashcardSet;
    }

    @Test
    void getAllFlashcardFromSet() {
        List<Flashcard> flashcardList = createFlashcardList();
        FlashcardSet set = createFlashcardSet(flashcardList);
        when( flashcardSetRepository.findById(any()) ).thenReturn( Optional.of(set) );

        List<FlashcardResponse> responseList = flashcardSetService.getAllFlashcardFromSet(anyLong());

        assertEquals( flashcardList.size(), responseList.size() );
        for (int i = 0; i < flashcardList.size(); i++) {
            assertEquals( flashcardList.get(i).getId(), responseList.get(i).getId() );
        }
    }

    @Test
    void getUsersSets() {
        FlashcardSet flashcardSet1 = createFlashcardSet(List.of());
        FlashcardSet flashcardSet2 = createFlashcardSet(List.of());
        List<FlashcardSet> flashcardSetList = List.of(flashcardSet1, flashcardSet2);
        when( flashcardSetRepository.findAllByAuthor(any()) ).thenReturn(flashcardSetList);

        List<FlashcardSet> responseList = flashcardSetService.getUsersSets(any());

        assertEquals( flashcardSetList, responseList );
    }

    @Test
    void getUsersSetsResponses() {
        FlashcardSet flashcardSet1 = createFlashcardSet(List.of());
        flashcardSet1.setId(1L);
        FlashcardSet flashcardSet2 = createFlashcardSet(List.of());
        flashcardSet2.setId(2L);
        List<FlashcardSet> flashcardSetList = List.of(flashcardSet1, flashcardSet2);
        when( flashcardSetRepository.findAllByAuthor(any()) ).thenReturn(flashcardSetList);

        List<FlashcardSetResponse> responseList = flashcardSetService.getUsersSetsResponses(any());

        assertEquals( flashcardSetList.size(), responseList.size() );
        for (int i = 0; i < flashcardSetList.size(); i++) {
            assertEquals( flashcardSetList.get(i).getId(), responseList.get(i).getId() );
        }
    }

    @Test
    void editSetAddFlashcard() {
        List<FlashcardChangeRequest> flashcardChangeRequestList = createFlashcardAddRequest();
        FlashcardSetChangeRequest flashcardSetChangeRequest = createFlashcardSetChangeRequest(flashcardChangeRequestList);
        FlashcardSet set = new FlashcardSet();
        set.setAuthor(new User());
        List<Flashcard> flashcardList = spy(new LinkedList<>());
        set.setFlashcards(flashcardList);
        when( flashcardSetRepository.findById(any()) ).thenReturn(Optional.of(set));

        StringResponse response = flashcardSetService.editSet(flashcardSetChangeRequest, new User());

        assertEquals("Set edited.", response.getBody());
        verify( flashcardList ).add(any());
        verify( flashcardList, times(0) ).set(anyInt(), any());
        verify( flashcardList, times(0) ).remove(any());
    }

    private FlashcardSetChangeRequest createFlashcardSetChangeRequest(List<FlashcardChangeRequest> flashcardList) {
        FlashcardSetChangeRequest changeRequest = new FlashcardSetChangeRequest();
        changeRequest.setId(1L);
        changeRequest.setName("name");
        changeRequest.setDescription("description");
        changeRequest.setFlashcardChangeList(flashcardList);

        return changeRequest;
    }

    private List<FlashcardChangeRequest> createFlashcardAddRequest() {
        FlashcardRequest flashcardRequest = new FlashcardRequest();
        flashcardRequest.setBack("back");
        flashcardRequest.setFront("front");

        FlashcardChangeRequest changeRequest = new FlashcardChangeRequest();
        changeRequest.setBody(flashcardRequest);

        return List.of(changeRequest);
    }

    @Test
    void editSetEditFlashcard() {
        List<FlashcardChangeRequest> flashcardChangeRequestList = createFlashcardEditRequest();
        FlashcardSetChangeRequest flashcardSetChangeRequest = createFlashcardSetChangeRequest(flashcardChangeRequestList);
        FlashcardSet set = new FlashcardSet();
        set.setAuthor(new User());
        List<Flashcard> flashcardList = spy(List.class);
        set.setFlashcards(flashcardList);
        when( flashcardSetRepository.findById(any()) ).thenReturn(Optional.of(set));
        when( flashcardList.indexOf(any()) ).thenReturn( 0 );
        when( flashcardList.set( anyInt(), any() ) ).thenReturn(null);

        StringResponse response = flashcardSetService.editSet(flashcardSetChangeRequest, new User());

        assertEquals("Set edited.", response.getBody());
        verify( flashcardList, times(0) ).add(any());
        verify( flashcardList ).set(anyInt(), any());
        verify( flashcardList, times(0) ).remove(any());
    }

    private List<FlashcardChangeRequest> createFlashcardEditRequest() {
        FlashcardRequest flashcardRequest = new FlashcardRequest();
        flashcardRequest.setBack("back");
        flashcardRequest.setFront("front");

        FlashcardChangeRequest changeRequest = new FlashcardChangeRequest();
        changeRequest.setId(1L);
        changeRequest.setBody(flashcardRequest);

        return List.of(changeRequest);
    }

    @Test
    void editSetDeleteFlashcard() {
        List<FlashcardChangeRequest> flashcardChangeRequestList = createFlashcardDeleteRequest();
        FlashcardSetChangeRequest flashcardSetChangeRequest = createFlashcardSetChangeRequest(flashcardChangeRequestList);
        FlashcardSet set = new FlashcardSet();
        set.setAuthor(new User());
        List<Flashcard> flashcardList = spy(new LinkedList<>());
        set.setFlashcards(flashcardList);
        when( flashcardSetRepository.findById(any()) ).thenReturn(Optional.of(set));

        StringResponse response = flashcardSetService.editSet(flashcardSetChangeRequest, new User());

        assertEquals("Set edited.", response.getBody());
        verify( flashcardList, times(0) ).add(any());
        verify( flashcardList, times(0) ).set(anyInt(), any());
        verify( flashcardList ).remove(any());
    }

    private List<FlashcardChangeRequest> createFlashcardDeleteRequest() {
        FlashcardChangeRequest changeRequest = new FlashcardChangeRequest();
        changeRequest.setId(1L);

        return List.of(changeRequest);
    }

    @Test
    void editWrongSetFlashcard() {
        List<FlashcardChangeRequest> flashcardChangeRequestList = createFlashcardEditRequest();
        FlashcardSetChangeRequest flashcardSetChangeRequest = createFlashcardSetChangeRequest(flashcardChangeRequestList);
        FlashcardSet set = new FlashcardSet();
        set.setAuthor(new User());
        set.getAuthor().setId(1L);
        List<Flashcard> flashcardList = spy(new LinkedList<>());
        set.setFlashcards(flashcardList);
        when( flashcardSetRepository.findById(any()) ).thenReturn(Optional.of(set));

        StringResponse response = flashcardSetService.editSet(flashcardSetChangeRequest, new User());

        assertEquals("Error. Try again later.", response.getBody());
        verify( flashcardList, times(0) ).add(any());
        verify( flashcardList, times(0) ).set(anyInt(), any());
        verify( flashcardList, times(0) ).remove(any());
    }

    @Test
    void getSetChangeResponseSuccess() {
        User author = new User();
        author.setId(1L);
        FlashcardSet set = new FlashcardSet();
        set.setId(1L);
        set.setAuthor( author );
        set.setFlashcards( List.of() );
        when( flashcardSetRepository.findById(anyLong()) ).thenReturn(Optional.of(set));

        FlashcardSetChangeResponse response = flashcardSetService.getSetChangeResponse(1L, author);

        assertEquals( set.getId(), response.getId() );
    }

    @Test
    void getSetChangeResponseWrongAuthor() {
        User author = new User();
        author.setId(1L);
        User user = new User();
        FlashcardSet set = new FlashcardSet();
        set.setId(1L);
        set.setAuthor( author );
        when( flashcardSetRepository.findById(anyLong()) ).thenReturn(Optional.of(set));

        Throwable exception = assertThrows( ForbiddenException.class, () ->
                flashcardSetService.getSetChangeResponse(1L, user));
    }
}