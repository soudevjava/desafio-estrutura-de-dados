import application.services.BibliotecaService;
import application.services.BibliotecaServiceImpl;
import infrastructure.persistence.FileManager;
import infrastructure.persistence.FileManagerImpl;
import infrastructure.repositories.BibliotecaRepository;
import infrastructure.repositories.BibliotecaRepositoryImpl;
import presentation.console.BibliotecaConsole;

public class Main {
    public static void main(String[] args) {
        FileManager fileManager = new FileManagerImpl();
        BibliotecaRepository repository = new BibliotecaRepositoryImpl(fileManager);
        BibliotecaService service = new BibliotecaServiceImpl(repository);
        BibliotecaConsole console = new BibliotecaConsole(service);
        console.iniciar();
    }
}