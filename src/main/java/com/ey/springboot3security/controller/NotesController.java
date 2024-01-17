package com.ey.springboot3security.controller;

import com.ey.springboot3security.entity.Notes;
import com.ey.springboot3security.repository.NotesRepository;
import com.ey.springboot3security.service.NotesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")

public class NotesController {

    @Autowired
    private NotesService notesService;
    @Autowired
    private NotesRepository notesRepository;


    // get all notes
    @GetMapping("/notes/all")
    public List<Notes> getNotes() {
        return notesService.getAllNotes();

    }

    // get by id
    @GetMapping("/notes/{id}")
    public ResponseEntity<Notes> getNoteById(@PathVariable int id) {
        Optional<Notes> note = notesRepository.findById(id);
        return note.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // create notes
    @PostMapping("/notes/add")
    Notes addNotes(@RequestBody Notes notes) {
        return notesService.saveNote(notes);
    }

    // delete by id
    @DeleteMapping("/notes/{id}")
    public ResponseEntity<?> deleteNotes(@PathVariable int id) {
        notesRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // update Notes
    @PutMapping("/notes/{id}")
    public ResponseEntity<?> updateNoteById(@PathVariable int id, @RequestBody Notes notes) {
        Optional<Notes> existingNotes = notesRepository.findById(id);

        if (existingNotes.isPresent()) {
            Notes existingNote = existingNotes.get();
            existingNote.setTitle(notes.getTitle());
            existingNote.setContent(notes.getContent());
            existingNote.setCategory(notes.getCategory());

            Notes updatedNotes = notesRepository.save(existingNote);
            return ResponseEntity.ok(updatedNotes);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

