package TrainTables.TableReaders;

import TrainTables.HtmlTable;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import TrainTables.TableReaders.utils.*;

public class Spravka93Reader implements TableReaderInterface {

    final static String regexDocHead = "(?<dhOrg>[А-ЯA-Z]{2,6})\\s+"
            + "(?<dhUTY>[А-ЯA-Z]{2,6})\\s+"
            + "(?<dhSpr>\\d{2,4})\\s+"
            + "(?<dhdate>\\d{2}.\\d{2})\\s+"
            + "(?<dhtime>\\d{2}-\\d{2})\\s+"
            + "(?<dhOrg2>[А-ЯA-Z]{2,6})\\s+"
            + "(?<dhnum>\\d{2,4})\\s+"
            + "(?<dhnpn>[А-ЯA-Z]{2,7}\\s+[А-ЯA-Z]{2,7}\\s+[А-ЯA-Z]{2,11}\\s+[А-ЯA-Z]{2})\\s+"
            + "(?<dhSt>[А-ЯA-Z]{2}.\\s+[А-ЯA-Z\\d+]{2,8})";

    final static String regexTHead = "(?<hnum>[А-ЯA-Z]{5})\\s+"
            + "(?<hidx>[А-ЯA-Z]{6})\\s+"
            + "(?<hstate>[А-ЯA-Z]{4})\\s+"
            + "(?<hdate>[А-ЯA-Z]{4})\\s+"
            + "(?<htime>[А-ЯA-Z]{5})\\s+"
            + "(?<hpark>[А-ЯA-Z]{4})\\s+"
            + "(?<hvag>[А-ЯA-Z]{3})\\s+"
            + "(?<hudl>[А-ЯA-Z]{3})\\s+"
            + "(?<hbrutt>[А-ЯA-Z]{5})";

    final static String regexTBody = "(?<bnum>\\d{4})\\s+"
            + "(?<bidx>\\d{4}\\s+\\d{2,3}\\s\\d{4})\\s+"
            + "(?<bstate>[А-ЯA-Z]{2,4})\\s+"
            + "(?<bdate>\\d{2}.\\d{2})\\s+"
            + "(?<btime>\\d{2}-\\d{2})\\s+"
            + "(?<bpark>\\d{2}\\/\\d{2})\\s+"
            + "(?<bvag>\\d{1,2})\\s+"
            + "(?<budl>\\d{1,2})\\s+"
            + "(?<bbrutt>\\d{2,4})";

    @Override
    public HtmlTable processFile(String fileName) {
        String str = null;
        String f = null;
        Pattern pattern;
        Matcher matcher;
        boolean reading = false;
        /*
        * пока условно будем считать что файл всегда есть!
         */
        try (FileInputStream fis = new FileInputStream(fileName)) {

            System.out.println("Размер файла: " + fis.available() + " байт(а)");

            byte[] buffer = new byte[fis.available()];

            // считаем файл в буфер
            fis.read(buffer, 0, fis.available());

            str = new String(buffer, "CP1251");

            f = TextReplace.getText(str);

        } catch (IOException ex) {
            Logger.getLogger(Spravka93Reader.class.getName()).log(Level.SEVERE, null, ex);
        }

        HtmlTable result = new HtmlTable();

        
        pattern = Pattern.compile(regexDocHead);
        matcher = pattern.matcher(f);

        boolean tableHeaderProcessed = false;

        while (matcher.find()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                result.addCell(matcher.group(i));
            }

            if (!tableHeaderProcessed) {
                tableHeaderProcessed = true;
                result.markCurrentRowAsDocHeader();
            }

            result.advanceToNextRow();
        }

        pattern = Pattern.compile(regexTHead);
        matcher = pattern.matcher(f);
        tableHeaderProcessed = false;

        while (matcher.find()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                result.addCell(matcher.group(i));
            }

            if (!tableHeaderProcessed) {
                tableHeaderProcessed = true;
                result.markCurrentRowAsHeader();
            }

            result.advanceToNextRow();
        }

        pattern = Pattern.compile(regexTBody);
        matcher = pattern.matcher(f);

        while (matcher.find()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                result.addCell(matcher.group(i));
            }

            if (!tableHeaderProcessed) {
                tableHeaderProcessed = true;
                result.markCurrentRowAsHeader();
            }
            reading = true;
            result.advanceToNextRow();
        }
        if (reading == true) {
            System.out.println("can reading SPR93 " + result);
            return result;
        } else {
            System.out.println("can not reading SPR93 " + result);
            return null;
        }
    }

}
