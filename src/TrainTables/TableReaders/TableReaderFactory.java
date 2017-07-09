package TrainTables.TableReaders;
import java.util.Map;
import java.util.HashMap;
public class TableReaderFactory {
    public void registerReader(String readerName, TableReaderInterface reader) {
	readersMap.put(readerName, reader);
    }

    public TableReaderInterface getReader(String readerName) {
	return readersMap.get(readerName);
    }

    private Map<String, TableReaderInterface> readersMap = new HashMap<>();
}
