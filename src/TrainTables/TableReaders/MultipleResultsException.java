
package TrainTables.TableReaders;

import TrainTables.HtmlTable;
import java.util.List;

public class MultipleResultsException extends Exception {
    MultipleResultsException(List<HtmlTable> multipleResults) {
	this.multipleResults = multipleResults;
    }

    public final List<HtmlTable> multipleResults;
}