package org.example.demo.service;

import org.example.demo.entity.Note;
import java.util.List;

public interface NoteService {
    List<Note> listAll();
    Note add(Note note);
    boolean deleteById(long id);
    void update (Note note);
    Note getById(long id);}





