package org.example.demo;

import org.example.demo.entity.Note;
import org.example.demo.repository.NoteRepository;
import org.example.demo.service.NoteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class NoteServiceTest {

    @Mock
    private NoteRepository noteRepository;

    @InjectMocks
    private NoteServiceImpl noteService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testListAll() {
        List<Note> notes = new ArrayList<>();
        notes.add(new Note());
        notes.add(new Note());

        when(noteRepository.findAll()).thenReturn(notes);

        List<Note> result = noteService.listAll();

        assertEquals(2, result.size());
        verify(noteRepository, times(1)).findAll();
    }

    @Test
    public void testAdd() {
        Note note = new Note();
        note.setTitle("Test Title");
        note.setContent("Test Content");

        when(noteRepository.save(any(Note.class))).thenReturn(note);

        Note result = noteService.add(note);

        assertNotNull(result);
        assertEquals("Test Title", result.getTitle());
        verify(noteRepository, times(1)).save(note);
    }

    @Test
    public void testDeleteById_NoteNotFound() {
        long noteId = 1L;
        doThrow(new IllegalArgumentException("Note with id " + noteId + " not found"))
                .when(noteRepository).deleteById(noteId);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            noteService.deleteById(noteId);
        });

        String expectedMessage = "Note with id " + noteId + " not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testUpdate_NoteNotFound() {
        Note note = new Note();
        note.setId(1L);
        note.setTitle("Updated Title");
        note.setContent("Updated Content");

        when(noteRepository.existsById(note.getId())).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            noteService.update(note);
        });

        String expectedMessage = "Note with id " + note.getId() + " not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testGetById_NoteNotFound() {
        long noteId = 1L;

        when(noteRepository.findById(noteId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            noteService.getById(noteId);
        });

        String expectedMessage = "Note with id " + noteId + " not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}