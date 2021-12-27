package persistence.fs;

import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.ejb.SessionSynchronization;
import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.transaction.Status;
import javax.transaction.TransactionScoped;
import javax.transaction.TransactionSynchronizationRegistry;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.rmi.RemoteException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;


@Named("haha")
@TransactionScoped
@Stateful
public class TransactionalFileSystemManager implements FileSystemManager,SessionSynchronization {
    private final Deque<Command> commands = new ArrayDeque<>();

    public void createFile(InputStream content, Path path, String name) throws IOException {
        CreateFile command = CreateFile.execute(content, path, name);
        System.out.println("Transazione in corso.");
        commands.addLast(command);
    }

    public void deleteFile(Path path) throws IOException {
        DeleteFile command = DeleteFile.execute(path);
        commands.addLast(command);
    }

    private void commit() throws IOException{
        for(Command c : commands){
            c.confirm();
        }
    }

    private void rollback() throws IOException{
        Iterator<Command> it = commands.descendingIterator();
        while (it.hasNext()) {
            Command c = it.next();
            c.undo();
        }
    }

    @Override
    public void afterBegin() throws EJBException{

    }

    @Override
    public void beforeCompletion() throws EJBException{

    }

    @Override
    public void afterCompletion(boolean commitSucceeded) throws EJBException{
        System.out.println("Sono in after completion!");
        if(commitSucceeded){
            try {
                commit();
            } catch (IOException e) {
                throw new EJBException(e);
            }
        }
        else {
            try {
                rollback();
            } catch (IOException e) {
                throw new EJBException(e);
            }
        }
    }
}
