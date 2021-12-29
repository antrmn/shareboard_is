package persistence.fs;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class DeleteFile implements Command{
    private Path pathOriginal;
    private Path pathMarked;
    private boolean completed = false;

    private DeleteFile() {
    }

    public static DeleteFile execute(Path path) throws IOException {
        DeleteFile df = new DeleteFile();
        df.pathOriginal = path;
        df.pathMarked = path.resolveSibling("delete_" + df.pathOriginal);
        Files.move(path, path.resolveSibling("delete_"+path.getFileName()));
        return df;
    }

    @Override
    public void undo() throws IOException {
        if (!completed) {
            Files.move(pathMarked, pathOriginal);
            completed = true;
        }
    }

    @Override
    public void confirm() throws IOException {
        if (!completed) {
            boolean successful = Files.deleteIfExists(pathMarked);
            if (!successful) {
                //log?
            }
        }
        completed = true;
    }
}
