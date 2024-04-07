package uk.ac.nulondon;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static uk.ac.nulondon.DiskUtilities.fileExists;
import static uk.ac.nulondon.CollectionUtilities.*;
import static uk.ac.nulondon.Utilities.clamp;

/**
 * a class representing an immutable image.
 * <br><br>
 * notes:
 * <br><br>
 * the origin is the pixel in the top left corner of the image.
 * <br><br>
 * a pixel at (x,y) is `x` pixels to the right of the origin and `y` pixels below the origin.
 * <br><br>
 * a column of an image can be represented by a list of the column's pixels' colors ordered from the top-most pixel's color to the bottom-most pixel's color.
 * <br><br>
 * a row of an image can be represented by a list of the row's pixels' colors ordered from the left-most pixel's color to the right-most pixel's color.
 * <br><br>
 * an image can be represented by a list of list representations of columns ordered from left-most column to right-most column.
 * <br><br>
 * an image can be represented by a list of list representations of rows ordered from top-most row to bottom-most row.
 */
public class Image {
    /**
     * `bufferedImage` is never null nor mutated.
     */
    private final BufferedImage bufferedImage;

    ///////////////////////////////////////////////////////////////////////////
    // CONSTRUCTION
    ///////////////////////////////////////////////////////////////////////////

    /**
     * constructs a new image from the given non-null buffered image.
     */
    private Image(BufferedImage bufferedImage) {
        Objects.requireNonNull(bufferedImage);
        this.bufferedImage = bufferedImage;
    }

    /**
     * returns a new image representing the image located at `filePath`.
     * if no file exists at `filePath`, an exception is thrown.
     * if a file exists at `filePath` but the file is not an image, an exception is thrown.
     */
    public static Image fromFilePath(String filePath) throws IOException {
        assert fileExists(filePath);

        File file = new File(filePath);
        return new Image(ImageIO.read(file));
    }

    /**
     * returns a new image representing the image described by the list of list representations of columns.
     */
    public static Image fromColumns(List<List<Color>> columns) {
        Objects.requireNonNull(columns);
        assert !columns.isEmpty();

        int height = columns.getFirst().size();
        int width = columns.size();
        boolean areColumnsSameSize = columns
                .stream()
                .allMatch(column -> column.size() == height);

        assert areColumnsSameSize;
        assert height != 0;

        BufferedImage bufferedImage = new BufferedImage(width, height, TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color color = columns.get(x).get(y);
                int rgb = color.getRGB();
                bufferedImage.setRGB(x, y, rgb);
            }
        }
        return new Image(bufferedImage);
    }

    /**
     * returns a new image representing the image described by the list of list representations of rows.
     */
    public static Image fromRows(List<List<Color>> rows) {
        List<List<Color>> columns = transpose(rows);
        return fromColumns(columns);
    }

    ///////////////////////////////////////////////////////////////////////////
    // COLUMNS
    ///////////////////////////////////////////////////////////////////////////

    /**
     * returns a list representation of the x'th column.
     * x must be in the range [0, width).
     */
    public List<Color> getColumn(int x) {
        assert existsColumnAt(x);

        return initializeList(
                getHeight(),
                y -> getColorAt(x, y)
        );
    }

    /**
     * returns a list of list representations of the columns.
     */
    public List<List<Color>> getColumns() {
        return initializeList(
                getWidth(),
                this::getColumn
        );
    }

    /**
     * returns a new image without the x'th column.
     * x must be in the range [0, width).
     */
    public Image removeColumn(int x) {
        assert existsColumnAt(x);

        List<List<Color>> columns = getColumns();
        columns.remove(x);
        return fromColumns(columns);
    }

    /**
     * returns a new image with `column` inserted before the x'th column.
     * x must be in the range [0, width].
     * if x == width, `column` is inserted after the rightmost column.
     * the length of `column` must equal this image's height.
     */
    public Image addColumn(int x, List<Color> column) {
        assert existsColumnAt(x) || x == getWidth();
        assert column.size() == getHeight();

        List<List<Color>> columns = getColumns();
        columns.add(x, column);
        return fromColumns(columns);
    }

    ///////////////////////////////////////////////////////////////////////////
    // ROWS
    ///////////////////////////////////////////////////////////////////////////

    /**
     * returns a list representation of the y'th row.
     * y must be in the range [0, height)
     */
    public List<Color> getRow(int y) {
        assert existsRowAt(y);

        return initializeList(
                getWidth(),
                x -> getColorAt(x, y)
        );
    }

    /**
     * returns a list of list representations of the rows.
     */
    public List<List<Color>> getRows() {
        return initializeList(
                getHeight(),
                this::getRow
        );
    }

    /**
     * returns a new image without the y'th row.
     * y must be in the range [0, height)
     */
    public Image removeRow(int y) {
        assert existsRowAt(y);

        List<List<Color>> rows = getRows();
        rows.remove(y);
        return fromRows(rows);
    }

    /**
     * returns a new image with `row` inserted before the y'th row.
     * y must be in the range [0, height].
     * if y == height, `row` is inserted after the bottommost row.
     * the length of `row` must equal this image's width.
     */
    public Image addRow(int y, List<Color> row) {
        assert existsRowAt(y) || y == getHeight();
        assert row.size() == getWidth();

        List<List<Color>> rows = getRows();
        rows.add(y, row);
        return fromRows(rows);
    }

    ///////////////////////////////////////////////////////////////////////////

    /**
     * returns the color of the pixel at (x,y).
     * i.e. returns the color of the pixel in the x'th column and y'th row.
     * x must be in the range [0, width).
     * y must be in the range [0, height).
     */
    public Color getColorAt(int x, int y) {
        assert existsColumnAt(x) && existsRowAt(y);

        int rgb = bufferedImage.getRGB(x, y);
        return new Color(rgb);
    }

    /**
     * returns a new image whose color at (x,y) is `color`.
     * i.e. returns a new image whose color in the x'th column and y'th row is `color`.
     * x must be in the range [0, width).
     * y must be in the range [0, height).
     */
    public Image setColorAt(int x, int y, Color color) {
        assert existsColumnAt(x) && existsRowAt(y);

        List<List<Color>> columns = getColumns();
        List<Color> column = columns.get(x);
        column.set(y, color);
        return fromColumns(columns);
    }

    /**
     * returns the number of pixels on the x-axis.
     * i.e. returns the number of columns.
     */
    public int getWidth() {return bufferedImage.getWidth();}

    /**
     * returns the number of pixels on the y-axis.
     * i.e. returns the number of rows.
     */
    public int getHeight() {return bufferedImage.getHeight();}

    /**
     * returns a buffered image representation of this image.
     */
    public BufferedImage getBufferedImage() {
        // if we directly return `bufferedImage`, `bufferedImage` can be mutated.
        // thus we return a deep copy of `bufferedImage`.
        ColorModel colorModel = bufferedImage.getColorModel();
        boolean isAlphaPremultiplied = colorModel.isAlphaPremultiplied();
        WritableRaster raster = bufferedImage.copyData(null);
        return new BufferedImage(
                colorModel,
                raster,
                isAlphaPremultiplied,
                null
        );
    }

    /**
     * returns a string representation of this image.
     * the i'th line of the string is a list representation of the i'th row.
     * each color is represented by a list of its rgb components.
     */
    @Override
    public String toString() {
        List<String> rowStrings = map(
                getRows(),
                row -> row.toString()
                          .replace("java.awt.Color", "")
        );
        return String.join("\n", rowStrings);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Image other) {
            return this.getRows().equals(other.getRows());
        }
        return false;
    }

    ///////////////////////////////////////////////////////////////////////////
    // ENERGY
    ///////////////////////////////////////////////////////////////////////////

    public List<List<Double>> getEnergyRows() {
        return initializeList(
                getHeight(),
                this::getEnergyRow
        );
    }

    public List<Double> getEnergyRow(int y) {
        assert existsRowAt(y);

        return initializeList(
                getWidth(),
                x -> getEnergyAt(x, y)
        );
    }

    public List<List<Double>> getEnergyColumns() {
        return initializeList(
                getWidth(),
                this::getEnergyColumn
        );
    }

    public List<Double> getEnergyColumn(int x) {
        assert existsColumnAt(x);

        return initializeList(
                getHeight(),
                y -> getEnergyAt(x, y)
        );
    }

    public double getBrightnessAt(int x, int y) {
        // suppose we wanted to find the energy of a pixel on the leftmost column.
        // a pixel's energy is a function of the brightnesses of the adjacent pixels.
        // if a pixel is on the leftmost column, there is no pixel to the left to find the brightness of.
        // thus we clamp `x` and `y` to valid indices.
        // this addresses edge cases (pixels literally on an edge of the image)
        int minIndexX = 0;
        int minIndexY = 0;
        int maxIndexX = getWidth() - 1;
        int maxIndexY = getHeight() - 1;
        x = clamp(x, minIndexX, maxIndexX);
        y = clamp(y, minIndexY, maxIndexY);

        Color color = getColorAt(x, y);
        double red = color.getRed();
        double green = color.getGreen();
        double blue = color.getBlue();
        return (red + green + blue) / 3.0;
    }

    public static double getEnergyOfList(List<Double> brightnesses) {
        assert brightnesses.size() == 3;

        double a = brightnesses.get(0);
        double b = brightnesses.get(1);
        double c = brightnesses.get(2);
        return a + (2 * b) + c;
    }

    public double getHorizontalEnergyAt(int x, int y) {
        assert existsColumnAt(x) && existsRowAt(y);

        List<Double> leftColumnBrightnesses = List.of(
                getBrightnessAt(x - 1, y - 1),
                getBrightnessAt(x - 1, y),
                getBrightnessAt(x - 1, y + 1)
        );
        List<Double> rightColumnBrightnesses = List.of(
                getBrightnessAt(x + 1, y - 1),
                getBrightnessAt(x + 1, y),
                getBrightnessAt(x + 1, y + 1)
        );
        double leftColumnEnergy = getEnergyOfList(leftColumnBrightnesses);
        double rightColumnEnergy = getEnergyOfList(rightColumnBrightnesses);
        return leftColumnEnergy - rightColumnEnergy;
    }

    public double getVerticalEnergyAt(int x, int y) {
        assert existsColumnAt(x) && existsRowAt(y);

        // @formatter:off
        List<Double> topRowBrightnesses = List.of(
                getBrightnessAt(x - 1, y + 1),
                getBrightnessAt(x,     y + 1),
                getBrightnessAt(x + 1, y + 1)
        );
        List<Double> bottomRowBrightnesses = List.of(
                getBrightnessAt(x - 1, y - 1),
                getBrightnessAt(x,     y - 1),
                getBrightnessAt(x + 1, y - 1)
        );
        // @formatter:on
        double topRowEnergy = getEnergyOfList(topRowBrightnesses);
        double bottomRowEnergy = getEnergyOfList(bottomRowBrightnesses);
        return topRowEnergy - bottomRowEnergy;
    }

    public double getEnergyAt(int x, int y) {
        assert existsColumnAt(x) && existsRowAt(y);

        double horizontalEnergy = getHorizontalEnergyAt(x, y);
        double verticalEnergy = getVerticalEnergyAt(x, y);
        return Math.hypot(horizontalEnergy, verticalEnergy);
    }

    ///////////////////////////////////////////////////////////////////////////
    // EXISTENCE
    ///////////////////////////////////////////////////////////////////////////

    public boolean existsColumnAt(int x) {return 0 <= x && x < getWidth();}

    public boolean existsRowAt(int y) {return 0 <= y && y < getHeight();}
}
