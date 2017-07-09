package TrainTables.TableReaders;

import TrainTables.HtmlTable;

public interface TableReaderInterface {

    HtmlTable processFile(String fileName) throws MultipleResultsException;
}
