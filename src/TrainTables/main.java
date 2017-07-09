package TrainTables;

import TrainTables.TableReaders.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.Map;

public class main {

    static String p = "c:\\testFolder\\in";

    public static void main(String[] args) {
        // init composite reader - register all reader types
        tableReader.registerReader(new Spravka93Reader());
        tableReader.registerReader(new Spravka95Reader());
        tableReader.registerReader(new Spravka02Reader());

        ///////////////////////////////////////////
        try (WatchService service = FileSystems.getDefault().newWatchService()) {
            Map<WatchKey, Path> keyMap = new HashMap<>();
            Path path = Paths.get(p);

            keyMap.put(path.register(service,
                                        StandardWatchEventKinds.ENTRY_CREATE
                    //                  ,StandardWatchEventKinds.ENTRY_DELETE
                    //                  ,StandardWatchEventKinds.ENTRY_MODIFY
            ), path);

            WatchKey watchKey;
            do {
                watchKey = service.take();
                Path eventDir = keyMap.get(watchKey);

                for (WatchEvent<?> event : watchKey.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();
                    Path eventPath = (Path) event.context();
                    System.out.println(eventDir + " : " + kind + " : " + eventPath);
                    File f = new File(eventDir + "\\" + eventPath);

                    boolean b;
                    do {
                        b = true;
                        try (FileInputStream fis = new FileInputStream(f)) {
                            while (f.canRead() && f.canWrite() && f.exists()) {
                                readingFile(eventDir + "\\" + eventPath);
                                break;
                            }
                        } catch (IOException ex) {
                            System.out.println("Файл занят " + ex);
                            b = false;
                        }
                    } while (!b);
                }
            } while (watchKey.reset());
        } catch (Exception e) {
            System.out.println("exception on WatchService " + e);
        }
        ///////////////////////////////////////////           
    }

    static final CompositeReader tableReader = new CompositeReader();

    private static void readingFile(String path) {
        // test
        String fileNameToTest;
        fileNameToTest = path;
        System.out.println("Using file name " + fileNameToTest);

        try {
            File f = new File(fileNameToTest);
            if (f.exists()) {
                HtmlTable result = tableReader.processFile(fileNameToTest);
                if (result != null) {
                    System.out.println(result.generateHtml());
                } else {
                    System.out.println("Could not detect input file type");
                }
            } else {
                System.out.println("File not found");
            }
        } catch (MultipleResultsException ex) {
            System.out.println("Error: multiple results");
            for (HtmlTable result : ex.multipleResults) {
                System.out.println(result);
            }
        }
    }
}
