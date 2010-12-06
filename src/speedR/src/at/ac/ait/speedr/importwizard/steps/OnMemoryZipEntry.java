/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.ac.ait.speedr.importwizard.steps;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 *
 * @author visnei
 */
public class OnMemoryZipEntry extends ZipEntry{

    private byte[] zippedData;

    public OnMemoryZipEntry(ZipEntry entry,byte[] zippedData) {
        super(entry.getName());
        this.zippedData = zippedData;
    }

    public ZipInputStream getUnzippedDataStream() throws IOException{
        ZipInputStream zip = new ZipInputStream(new ByteArrayInputStream(zippedData));
        zip.getNextEntry();
        return zip;
    }

}
