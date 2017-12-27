/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socketprogramming.ServerSide;import java.io.File;

/**
 *
 * @author manal
 */
public class AudioUtil {

    public static File getSoundFile(String fileName) {
        File soundFile = new File(fileName);
        if (!soundFile.exists() || !soundFile.isFile())
            throw new IllegalArgumentException("not a file: " + soundFile);
        return soundFile;
    }

}
