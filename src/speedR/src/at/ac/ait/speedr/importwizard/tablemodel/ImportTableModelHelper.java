/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.ait.speedr.importwizard.tablemodel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author visnei
 */
public class ImportTableModelHelper {

    private HashMap<Integer, File> columnfilemap = new HashMap<Integer, File>();
    private HashMap<Integer, BufferedWriter> columnwritermap = new HashMap<Integer, BufferedWriter>();

    public void saveRowDataSet(int line, String[] values) throws IOException {
        BufferedWriter writer;
        for (int i = 0; i < values.length; i++) {
            writer = columnwritermap.get(i);
            if (writer == null) {
                File temp = File.createTempFile(ImportTableModelHelper.class.getSimpleName(), ".col");
                temp.deleteOnExit();
                writer = new BufferedWriter(new FileWriter(temp));
                columnfilemap.put(i, temp);
                columnwritermap.put(i, writer);
            }

            writer.write(line+"\n");
            writer.write(values[i]+"\n");
        }
    }

    public void close() throws IOException{
        for(BufferedWriter writer:columnwritermap.values()){
            writer.flush();
            writer.close();
        }
    }

    public List<String> getColumnData(int column) throws FileNotFoundException{
        ArrayList<String> data = new ArrayList<String>();
        Scanner scanner = new Scanner(columnfilemap.get(column));
        scanner.useDelimiter("\n");

        int row = 0;
        int nextint;

        while(scanner.hasNextInt()){
            nextint = scanner.nextInt();
            while(row < nextint){
                data.add(null);
                row++;
            }
            data.add(scanner.next());
            row++;
        }
        scanner.close();
        return data;
    }
}
