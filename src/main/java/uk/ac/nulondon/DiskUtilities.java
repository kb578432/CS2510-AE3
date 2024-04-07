package uk.ac.nulondon;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DiskUtilities {
    /**
     * writes `bufferedImage` to the specified file path.
     */
    public static void writeToDisk(
            BufferedImage bufferedImage, String filePath) throws IOException {

        File newFile = new File(filePath);
        newFile.createNewFile();
        ImageIO.write(bufferedImage, "png", newFile);
    }

    /**
     * returns true if a file exists at `filePath`, else false.
     */
    public static boolean fileExists(String filePath) {
        return new File(filePath).exists();
    }
}
