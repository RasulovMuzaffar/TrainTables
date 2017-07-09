/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TrainTables.TableReaders.utils;

/**
 *
 * @author Muzaffar
 */
public class TextReplace {
    public static String getText(String str){
        String text = str.replace("A", "А").replace("B", "В").replace("C", "С").replace("E", "Е").
                    replace("H", "Н").replace("K", "К").replace("M", "М").replace("O", "О").
                    replace("P", "Р").replace("T", "Т").replace("X", "Х").replace("Y", "У");
        return text;
    }
}
