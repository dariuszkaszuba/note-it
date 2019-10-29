package dk.note.api;

import dk.note.Mapper;
import dk.note.api.viewmodel.NotebookViewModel;
import dk.note.db.NotebookRepository;
import dk.note.model.Notebook;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.ValidationException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notebooks")
@CrossOrigin
public class NotebookController {
    private NotebookRepository notebookRepository;
    private Mapper mapper;

    public NotebookController(NotebookRepository notebookRepository, Mapper mapper) {
        this.notebookRepository = notebookRepository;
        this.mapper = mapper;
    }

    @GetMapping("/all")
    public List<Notebook> all() {
        return this.notebookRepository.findAll();
    }

    @PostMapping
    public Notebook save(@RequestBody NotebookViewModel notebookViewModel,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException();
        }

        var notebookEntity = this.mapper.convertToNotebookEntity(notebookViewModel);

        this.notebookRepository.save(notebookEntity);

        return notebookEntity;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        this.notebookRepository.deleteById(UUID.fromString(id));
    }
}
