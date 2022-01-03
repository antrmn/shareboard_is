package persistence.fs;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

class CreateFile implements Command {
    private InputStream content;
    private Path path;
    private String name;
    private File file;
    private boolean completed = false;

    private CreateFile() {
    }
// pincopallino

    public static CreateFile execute(InputStream content, Path path, String name) throws IOException {
        CreateFile cf = new CreateFile();
        cf.content = content;
        cf.path = path;
        cf.name = name;
        cf.file = new File(path.toFile(), "new_" + name);
        Files.copy(content, cf.file.toPath());
        return cf;
    }

    @Override
    public void undo() throws IOException {
        if (!completed) {
            boolean successful = this.file.delete();
            if (!successful) {
                //log?
            }
            completed = true;
        }
    }

    @Override
    public void confirm() throws IOException {
        if (!completed) {
            Files.move(file.toPath(), file.toPath().resolveSibling(this.name));
        }
        completed = true;
    }
}
