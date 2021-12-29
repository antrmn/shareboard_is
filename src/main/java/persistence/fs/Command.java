package persistence.fs;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

interface Command {
    public void undo() throws IOException;
    public void confirm() throws IOException;
}
