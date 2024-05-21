package org.example.demo;

import org.example.demo.controller.NoteController;
import org.example.demo.entity.Note;
import org.example.demo.service.NoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NoteController.class)

public class NoteControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NoteService noteService;

    private List<Note> noteList;


    @BeforeEach
    void setUp() {
        noteList = new ArrayList<>();
        noteList.add(new Note(1L, "Note 1", "Content 1"));
        noteList.add(new Note(2L, "Note 2", "Content 2"));
    }

    @Test
    void testCreateNote() throws Exception {
        Note note = new Note();
        note.setTitle("Test Title");
        note.setContent("Test Content");

        when(noteService.add(any(Note.class))).thenReturn(note);

        mockMvc.perform(MockMvcRequestBuilders.post("/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"title\": \"Test Title\", \"content\": \"Test Content\" }"))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(note.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value(note.getContent()));
    }

    @Test
    void testUpdateNote() throws Exception {
        Note existingNote = new Note();
        existingNote.setId(1L);
        existingNote.setTitle("Existing Title");
        existingNote.setContent("Existing Content");

        Note updatedNote = new Note();
        updatedNote.setId(existingNote.getId());
        updatedNote.setTitle("Updated Title");
        updatedNote.setContent("Updated Content");

        when(noteService.getById(1L)).thenReturn(existingNote);
        doNothing().when(noteService).update(any(Note.class));

        mockMvc.perform(MockMvcRequestBuilders.put("/notes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"title\": \"Updated Title\", \"content\": \"Updated Content\" }"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteNote() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/notes/1"))
                .andExpect(status().isNoContent());

        verify(noteService, times(1)).deleteById(1L);
    }

    @Test
    void testGetAllNotes() throws Exception {
        List<Note> notes = new ArrayList<>();
        notes.add(new Note(1L, "Note 1", "Content 1"));
        notes.add(new Note(2L, "Note 2", "Content 2"));

        when(noteService.listAll()).thenReturn(notes);

        mockMvc.perform(MockMvcRequestBuilders.get("/notes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(notes.size()));
    }

    @Test
    void testGetNoteById() throws Exception {
        Note note = new Note(1L, "Test Note", "Test Content");

        when(noteService.getById(1L)).thenReturn(note);

        mockMvc.perform(MockMvcRequestBuilders.get("/notes/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(note.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(note.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value(note.getContent()));
    }
}
