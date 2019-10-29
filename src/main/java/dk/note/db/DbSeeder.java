package dk.note.db;

import dk.note.model.Note;
import dk.note.model.Notebook;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "noteit.db.recreate", havingValue = "true")
public class DbSeeder implements CommandLineRunner {

    private NotebookRepository notebookRepository;
    private NoteRepository noteRepository;

    public DbSeeder(NotebookRepository notebookRepository, NoteRepository noteRepository) {
        this.notebookRepository = notebookRepository;
        this.noteRepository = noteRepository;
    }

    @Override
    public void run(String... args) {
        this.notebookRepository.deleteAll();
        this.noteRepository.deleteAll();

        var defaultNotebook = new Notebook("Default");
        this.notebookRepository.save(defaultNotebook);

        var quotesNotebook = new Notebook("Quotes");
        this.notebookRepository.save(quotesNotebook);

        var note = new Note("Hello", "Welcome to Note", defaultNotebook);
        this.noteRepository.save(note);

        var quoteNote = new Note("Latin Quote", "Carpe Diem", quotesNotebook);
        this.noteRepository.save(quoteNote);

        System.out.println("Initialized database");
    }
}
