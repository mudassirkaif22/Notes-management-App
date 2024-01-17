package com.ey.springboot3security.service;


import com.ey.springboot3security.entity.Notes;
import com.ey.springboot3security.repository.NotesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotesService {

    @Autowired
    private NotesRepository notesRepository;

    public List<Notes> getAllNotes() {
        return notesRepository.findAll();
    }

    public Notes saveNote(Notes note) {
        return notesRepository.save(note);
    }

}
