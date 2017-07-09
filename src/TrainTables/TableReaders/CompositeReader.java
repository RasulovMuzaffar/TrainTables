
package TrainTables.TableReaders;

import TrainTables.HtmlTable;
import java.util.List;
import java.util.ArrayList;

public class CompositeReader implements TableReaderInterface {
    @Override
    public HtmlTable processFile(String fileName) throws MultipleResultsException {
	List<HtmlTable> results = new ArrayList<>();
	for (TableReaderInterface reader: readersList) {
	    HtmlTable table = reader.processFile(fileName);
	    if (table != null) {
		results.add(table);
                break;
	    }
	}

	switch (results.size()) {
	    case 0:
		return null; // no results, input file not recognized
	    case 1:
		return results.get(0); // the only result
	    default:
		throw new MultipleResultsException(results);
	}
    }

    public void registerReader(TableReaderInterface reader) {
	readersList.add(reader);
    }

    private List<TableReaderInterface> readersList = new ArrayList<>();
}
