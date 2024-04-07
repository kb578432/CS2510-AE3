package uk.ac.nulondon;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import static java.awt.Color.*;
import static java.awt.Color.GREEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static uk.ac.nulondon.Images.*;

class TestImage {
    ///////////////////////////////////////////////////////////////////////////
    // CONSTRUCTION
    ///////////////////////////////////////////////////////////////////////////

    @ParameterizedTest
    @MethodSource("fromColumnsArguments")
    void fromColumns(List<List<Color>> columns) {
        Image image = Image.fromColumns(columns);
        BufferedImage bufferedImage = image.getBufferedImage();
        int width = image.getWidth();
        int height = image.getHeight();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int rgb = bufferedImage.getRGB(x, y);
                Color actualColor = new Color(rgb);
                Color expectedColor = columns.get(x).get(y);
                assertThat(actualColor).isEqualTo(expectedColor);
            }
        }
    }

    public static Stream<List<List<Color>>> fromColumnsArguments() {
        return imagesAsColumns.stream();
    }

    @ParameterizedTest
    @MethodSource("fromRowsArguments")
    void fromRows(List<List<Color>> rows) {
        Image image = Image.fromRows(rows);
        BufferedImage bufferedImage = image.getBufferedImage();
        int width = image.getWidth();
        int height = image.getHeight();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int rgb = bufferedImage.getRGB(x, y);
                Color actualColor = new Color(rgb);
                Color expectedColor = rows.get(y).get(x);
                assertThat(actualColor).isEqualTo(expectedColor);
            }
        }
    }

    public static Stream<List<List<Color>>> fromRowsArguments() {
        return imagesAsRows.stream();
    }

    ///////////////////////////////////////////////////////////////////////////
    // COLUMNS
    ///////////////////////////////////////////////////////////////////////////

    @ParameterizedTest
    @MethodSource("addColumnArguments")
    void addColumn(
            Image image,
            int x,
            List<Color> column,
            Image expectedImage
    ) {
        Image actualImage = image.addColumn(x, column);
        assertThat(actualImage).isEqualTo(expectedImage);
    }

    public static Stream<Arguments> addColumnArguments() {
        Image image = getImage(0);
        List<Color> column = List.of(GREEN, GREEN, GREEN);
        return Stream.of(
                Arguments.of(
                        image, 0,
                        column,
                        Image.fromRows(
                                List.of(
                                        List.of(GREEN, BLUE, BLUE, RED),
                                        List.of(GREEN, RED, BLUE, RED),
                                        List.of(GREEN, RED, RED, RED)
                                )
                        )
                ),
                Arguments.of(
                        image, 1,
                        column,
                        Image.fromRows(
                                List.of(
                                        List.of(BLUE, GREEN, BLUE, RED),
                                        List.of(RED, GREEN, BLUE, RED),
                                        List.of(RED, GREEN, RED, RED)
                                )
                        )
                ),
                Arguments.of(
                        image, 2,
                        column,
                        Image.fromRows(
                                List.of(
                                        List.of(BLUE, BLUE, GREEN, RED),
                                        List.of(RED, BLUE, GREEN, RED),
                                        List.of(RED, RED, GREEN, RED)
                                )
                        )
                ),
                Arguments.of(
                        image, 3,
                        column,
                        Image.fromRows(
                                List.of(
                                        List.of(BLUE, BLUE, RED, GREEN),
                                        List.of(RED, BLUE, RED, GREEN),
                                        List.of(RED, RED, RED, GREEN)
                                )
                        )
                )
        );
    }

    @ParameterizedTest
    @MethodSource("removeColumnArguments")
    void removeColumn(Image image, int x, Image expectedImage) {
        Image actualImage = image.removeColumn(x);
        assertThat(actualImage).isEqualTo(expectedImage);
    }

    public static Stream<Arguments> removeColumnArguments() {
        Image image = getImage(0);
        return Stream.of(
                Arguments.of(
                        image, 0,
                        Image.fromRows(
                                List.of(
                                        List.of(BLUE, RED),
                                        List.of(BLUE, RED),
                                        List.of(RED, RED)
                                )
                        )

                ),
                Arguments.of(
                        image, 1,
                        Image.fromRows(
                                List.of(
                                        List.of(BLUE, RED),
                                        List.of(RED, RED),
                                        List.of(RED, RED)
                                )
                        )
                ),
                Arguments.of(
                        image, 2,
                        Image.fromRows(
                                List.of(
                                        List.of(BLUE, BLUE),
                                        List.of(RED, BLUE),
                                        List.of(RED, RED)
                                )
                        )
                )
        );

    }

    @ParameterizedTest
    @MethodSource("getColumnArguments")
    void getColumn(Image image, int x, List<Color> expectedColumn) {
        List<Color> actualColumn = image.getColumn(x);
        assertThat(actualColumn).isEqualTo(expectedColumn);
    }

    public static Stream<Arguments> getColumnArguments() {
        Image image = getImage(0);
        List<List<Color>> columns = imagesAsColumns.getFirst();
        return Stream.of(
                Arguments.of(image, 0, columns.get(0)),
                Arguments.of(image, 1, columns.get(1)),
                Arguments.of(image, 2, columns.get(2))
        );
    }

    @ParameterizedTest
    @MethodSource("getColumnsArguments")
    void getColumns(Image image, List<List<Color>> expectedColumns) {
        List<List<Color>> actualColumns = image.getColumns();
        assertThat(actualColumns).isEqualTo(expectedColumns);
    }

    public static Stream<Arguments> getColumnsArguments() {
        return Stream.of(
                Arguments.of(getImage(0), imagesAsColumns.get(0)),
                Arguments.of(getImage(1), imagesAsColumns.get(1)),
                Arguments.of(getImage(2), imagesAsColumns.get(2))
        );
    }

    ///////////////////////////////////////////////////////////////////////////
    // ROWS
    ///////////////////////////////////////////////////////////////////////////

    @ParameterizedTest
    @MethodSource("addRowArguments")
    void addRow(
            Image image,
            int y,
            List<Color> row,
            Image expectedImage
    ) {
        Image actualImage = image.addRow(y, row);
        assertThat(actualImage).isEqualTo(expectedImage);
    }

    public static Stream<Arguments> addRowArguments() {
        Image image = getImage(0);
        List<Color> row = List.of(GREEN, GREEN, GREEN);
        return Stream.of(
                Arguments.of(
                        image, 0,
                        row,
                        Image.fromRows(
                                List.of(
                                        row,
                                        List.of(BLUE, BLUE, RED),
                                        List.of(RED, BLUE, RED),
                                        List.of(RED, RED, RED)
                                )
                        )
                ),
                Arguments.of(
                        image, 1,
                        row,
                        Image.fromRows(
                                List.of(
                                        List.of(BLUE, BLUE, RED),
                                        row,
                                        List.of(RED, BLUE, RED),
                                        List.of(RED, RED, RED)
                                )
                        )
                ),
                Arguments.of(
                        image, 2,
                        row,
                        Image.fromRows(
                                List.of(
                                        List.of(BLUE, BLUE, RED),
                                        List.of(RED, BLUE, RED),
                                        row,
                                        List.of(RED, RED, RED)
                                )
                        )
                ),
                Arguments.of(
                        image, 3,
                        row,
                        Image.fromRows(
                                List.of(
                                        List.of(BLUE, BLUE, RED),
                                        List.of(RED, BLUE, RED),
                                        List.of(RED, RED, RED),
                                        row
                                )
                        )
                )
        );
    }

    @ParameterizedTest
    @MethodSource("removeRowArguments")
    void removeRow(Image image, int y, Image expectedImage) {
        Image actualImage = image.removeRow(y);
        assertThat(actualImage).isEqualTo(expectedImage);
    }

    public static Stream<Arguments> removeRowArguments() {
        Image image = getImage(0);
        return Stream.of(
                Arguments.of(
                        image, 0,
                        Image.fromRows(
                                List.of(
                                        List.of(RED, BLUE, RED),
                                        List.of(RED, RED, RED)
                                )
                        )

                ),
                Arguments.of(
                        image, 1,
                        Image.fromRows(
                                List.of(
                                        List.of(BLUE, BLUE, RED),
                                        List.of(RED, RED, RED)
                                )
                        )
                ),
                Arguments.of(
                        image, 2,
                        Image.fromRows(
                                List.of(
                                        List.of(BLUE, BLUE, RED),
                                        List.of(RED, BLUE, RED)
                                )
                        )
                )
        );

    }

    @ParameterizedTest
    @MethodSource("getRowArguments")
    void getRow(Image image, int y, List<Color> expectedRow) {
        List<Color> actualRow = image.getRow(y);
        assertThat(actualRow).isEqualTo(expectedRow);
    }

    public static Stream<Arguments> getRowArguments() {
        Image image = getImage(0);
        List<List<Color>> rows = imagesAsRows.getFirst();
        return Stream.of(
                Arguments.of(image, 0, rows.get(0)),
                Arguments.of(image, 1, rows.get(1)),
                Arguments.of(image, 2, rows.get(2))
        );
    }

    @ParameterizedTest
    @MethodSource("getRowsArguments")
    void getRows(Image image, List<List<Color>> expectedRows) {
        List<List<Color>> actualRows = image.getRows();
        assertThat(actualRows).isEqualTo(expectedRows);
    }

    public static Stream<Arguments> getRowsArguments() {
        return Stream.of(
                Arguments.of(getImage(0), imagesAsRows.get(0)),
                Arguments.of(getImage(1), imagesAsRows.get(1)),
                Arguments.of(getImage(2), imagesAsRows.get(2))
        );
    }

    ///////////////////////////////////////////////////////////////////////////

    @ParameterizedTest
    @CsvSource({
            "src/main/resources/script-generated-images/image-0.png",
            "src/main/resources/script-generated-images/image-1.png",
            "src/main/resources/script-generated-images/image-2.png",
            "src/main/resources/script-generated-images/image-3.png",
            "src/main/resources/script-generated-images/image-4.png",
            "src/main/resources/script-generated-images/image-5.png"
    })
    void getBufferedImage(String filePath) throws IOException {
        Image image = Image.fromFilePath(filePath);
        BufferedImage expectedBufferedImage = ImageIO.read(new File(filePath));
        BufferedImage actualBufferedImage = image.getBufferedImage();
        int width = expectedBufferedImage.getWidth();
        int height = expectedBufferedImage.getHeight();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color actualColor = new Color(actualBufferedImage.getRGB(x, y));
                Color expectedColor = new Color(expectedBufferedImage.getRGB(x, y));
                assertThat(actualColor).isEqualTo(expectedColor);
            }
        }
    }

    @ParameterizedTest
    @MethodSource("getColorAtArguments")
    void getColorAt(
            Image image,
            int x,
            int y,
            Color expectedColor
    ) {
        Color actualColor = image.getColorAt(x, y);
        assertThat(actualColor).isEqualTo(expectedColor);

    }

    public static Stream<Arguments> getColorAtArguments() {
        Image image = getImage(0);
        return Stream.of(
                Arguments.of(image, 0, 0, BLUE),
                Arguments.of(image, 1, 0, BLUE),
                Arguments.of(image, 2, 0, RED),
                Arguments.of(image, 0, 1, RED),
                Arguments.of(image, 1, 1, BLUE),
                Arguments.of(image, 2, 1, RED),
                Arguments.of(image, 0, 2, RED),
                Arguments.of(image, 1, 2, RED),
                Arguments.of(image, 2, 2, RED)
        );
    }

    @ParameterizedTest
    @CsvSource({
            "src/main/resources/script-generated-images/image-0.png, 3",
            "src/main/resources/script-generated-images/image-1.png, 3",
            "src/main/resources/script-generated-images/image-2.png, 3",
            "src/main/resources/script-generated-images/image-3.png, 3",
            "src/main/resources/script-generated-images/image-4.png, 3",
            "src/main/resources/script-generated-images/image-5.png, 3"
    })
    void getHeight(String filePath, int expectedHeight) throws IOException {
        Image image = Image.fromFilePath(filePath);
        int actualHeight = image.getHeight();
        assertThat(actualHeight).isEqualTo(expectedHeight);
    }

    @ParameterizedTest
    @CsvSource({
            "src/main/resources/script-generated-images/image-0.png, 3",
            "src/main/resources/script-generated-images/image-1.png, 3",
            "src/main/resources/script-generated-images/image-2.png, 3",
            "src/main/resources/script-generated-images/image-3.png, 3",
            "src/main/resources/script-generated-images/image-4.png, 3",
            "src/main/resources/script-generated-images/image-5.png, 3"
    })
    void getWidth(String filePath, int expectedWidth) throws IOException {
        Image image = Image.fromFilePath(filePath);
        int actualWidth = image.getWidth();
        assertThat(actualWidth).isEqualTo(expectedWidth);
    }

    @ParameterizedTest
    @MethodSource("setColorAtArguments")
    void setColorAt(
            Image image,
            int x,
            int y,
            Color color,
            Image expectedImage
    ) {
        Image actualImage = image.setColorAt(x, y, color);
        assertThat(actualImage).isEqualTo(expectedImage);
    }

    public static Stream<Arguments> setColorAtArguments() {
        Image image = getImage(0);
        return Stream.of(
                Arguments.of(
                        image, 0, 0, GREEN,
                        Image.fromRows(
                                List.of(
                                        List.of(GREEN, BLUE, RED),
                                        List.of(RED, BLUE, RED),
                                        List.of(RED, RED, RED)
                                )
                        )
                ),
                Arguments.of(
                        image, 1, 0, GREEN,
                        Image.fromRows(
                                List.of(
                                        List.of(BLUE, GREEN, RED),
                                        List.of(RED, BLUE, RED),
                                        List.of(RED, RED, RED)
                                )
                        )
                ),
                Arguments.of(
                        image, 2, 0, GREEN,
                        Image.fromRows(
                                List.of(
                                        List.of(BLUE, BLUE, GREEN),
                                        List.of(RED, BLUE, RED),
                                        List.of(RED, RED, RED)
                                )
                        )
                )
        );
    }

    @ParameterizedTest
    @MethodSource("testToStringArguments")
    void testToString(Image image, String expectedString) {
        String actualString = image.toString();
        assertThat(actualString).isEqualTo(expectedString);
    }

    public static Stream<Arguments> testToStringArguments() {
        return Stream.of(
                Arguments.of(
                        getImage(0),
                        String.join(
                                "\n",
                                "[[r=0,g=0,b=255], [r=0,g=0,b=255], [r=255,g=0,b=0]]",
                                "[[r=255,g=0,b=0], [r=0,g=0,b=255], [r=255,g=0,b=0]]",
                                "[[r=255,g=0,b=0], [r=255,g=0,b=0], [r=255,g=0,b=0]]"
                        )
                ),
                Arguments.of(
                        getImage(1),
                        String.join(
                                "\n",
                                "[[r=0,g=0,b=255], [r=255,g=0,b=0], [r=0,g=0,b=255]]",
                                "[[r=255,g=0,b=0], [r=255,g=0,b=0], [r=0,g=0,b=255]]",
                                "[[r=255,g=0,b=0], [r=255,g=0,b=0], [r=255,g=0,b=0]]"
                        )
                ),
                Arguments.of(
                        getImage(2),
                        String.join(
                                "\n",
                                "[[r=0,g=0,b=255], [r=0,g=0,b=255], [r=255,g=0,b=0]]",
                                "[[r=0,g=0,b=255], [r=255,g=0,b=0], [r=255,g=0,b=0]]",
                                "[[r=255,g=0,b=0], [r=255,g=0,b=0], [r=255,g=0,b=0]]"
                        )
                )
        );
    }
}