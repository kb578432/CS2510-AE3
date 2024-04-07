package uk.ac.nulondon;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import static java.awt.Color.BLUE;
import static java.awt.Color.RED;
import static uk.ac.nulondon.DiskUtilities.writeToDisk;
import static uk.ac.nulondon.CollectionUtilities.map;

public class Images {
    public static List<List<List<Color>>> imagesAsRows = List.of(
            List.of(
                    List.of(BLUE, BLUE, RED),
                    List.of(RED, BLUE, RED),
                    List.of(RED, RED, RED)
            ),
            List.of(
                    List.of(BLUE, RED, BLUE),
                    List.of(RED, RED, BLUE),
                    List.of(RED, RED, RED)
            ),
            List.of(
                    List.of(BLUE, BLUE, RED),
                    List.of(BLUE, RED, RED),
                    List.of(RED, RED, RED)
            ),
            List.of(
                    List.of(RED, BLUE, BLUE),
                    List.of(RED, RED, BLUE),
                    List.of(RED, RED, RED)
            ),
            List.of(
                    List.of(BLUE, RED, BLUE),
                    List.of(BLUE, RED, RED),
                    List.of(RED, RED, RED)
            ),
            List.of(
                    List.of(RED, BLUE, BLUE),
                    List.of(RED, BLUE, RED),
                    List.of(RED, RED, RED)
            )
    );

    public static List<List<List<Color>>> imagesAsColumns =
            map(imagesAsRows, CollectionUtilities::transpose);

    public static void main(String[] args) throws IOException {
        createImages();
    }

    public static void createImages() throws IOException {
        for (int i = 0; i < imagesAsRows.size(); i++) {
            List<List<Color>> rows = imagesAsRows.get(i);
            Image image = Image.fromRows(rows);
            String filePath = getImageFilePath(i);
            writeToDisk(image.getBufferedImage(), filePath);
        }
    }

    public static String getImageFilePath(int index) {
        boolean isIndexValid = 0 <= index && index < imagesAsRows.size();
        assert isIndexValid;
        return "src/main/resources/script-generated-images/image-" + index + ".png";
    }

    public static Image getImage(int index) {
        List<List<Color>> rows = imagesAsRows.get(index);
        return Image.fromRows(rows);
    }
}
