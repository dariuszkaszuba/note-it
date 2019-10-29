package dk.note.api;

import dk.note.Mapper;
import dk.note.api.viewmodel.NoteViewModel;
import dk.note.api.viewmodel.NotebookViewModel;
import dk.note.db.NoteRepository;
import dk.note.db.NotebookRepository;
import dk.note.model.Note;
import dk.note.model.Notebook;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notes")
@CrossOrigin
public class NoteController {
    private NoteRepository noteRepository;
    private NotebookRepository notebookRepository;
    private Mapper mapper;

    public NoteController(NoteRepository noteRepository, NotebookRepository notebookRepository, Mapper mapper) {
        this.noteRepository = noteRepository;
        this.notebookRepository = notebookRepository;
        this.mapper = mapper;
    }

    @GetMapping("/all")
    public List<NoteViewModel> all() {
        var notes = this.noteRepository.findAll();

        List<NoteViewModel> notesViewModel = notes.stream()
                .map(note -> this.mapper.convertToNoteViewModel(note))
                .collect(Collectors.toList());

        return notesViewModel;
    }

    @GetMapping("/getById/{id}")
    public NoteViewModel getById(@PathVariable String id){
        var note = this.noteRepository.findById(UUID.fromString(id)).orElse(null);

        if(note == null){
            throw new EntityNotFoundException();
        }

        var noteViewModel = this.mapper.convertToNoteViewModel(note);

        return noteViewModel;
    }

    @GetMapping("/getByNotebook/{notebookId}")
    public List<NoteViewModel> getByNotebook(@PathVariable String notebookId){
        List<Note> notes = new ArrayList<>();

        var notebook = this.notebookRepository.findById(UUID.fromString(notebookId));
        if(notebook.isPresent()){
            notes = this.noteRepository.findAllByNotebook(notebook.get());
        }

        var notesViewModel = notes.stream()
                .map(note -> this.mapper.convertToNoteViewModel(note))
                .collect(Collectors.toList());

        return notesViewModel;
    }

    @PostMapping()
    public Note save(@RequestBody NoteViewModel noteCreateViewModel, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new ValidationException();
        }

        var noteEntity = this.mapper.convertToNoteEntity(noteCreateViewModel);

        this.noteRepository.save(noteEntity);

        return noteEntity;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id){
        this.noteRepository.deleteById(UUID.fromString(id));
    }
}
