package uk.ac.nulondon;

import java.awt.Color;
import java.util.*;

import static java.awt.Color.BLUE;
import static java.awt.Color.RED;
import static uk.ac.nulondon.CollectionUtilities.*;

/**
 * a class enabling edits to images
 */
public class ImageEditor {
    private static final int NOT_HIGHLIGHTED = -1;

    private record ImageState(Image image, int highlightedColumnIndex) {
        public boolean isHighlighted() {
            return highlightedColumnIndex != NOT_HIGHLIGHTED;
        }
    }

    private final Stack<ImageState> imageStateStack = new Stack<>();

    public ImageEditor(Image image) {
        Objects.requireNonNull(image);
        ImageState imageState = new ImageState(image, NOT_HIGHLIGHTED);
        imageStateStack.push(imageState);
    }

    public void deleteHighlightedColumn() {
        assert isImageHighlighted();
        Image image = getImage();
        int highlightedColumnIndex = getHighlightedColumnIndex();
        Image newImage = image.removeColumn(highlightedColumnIndex);
        ImageState newImageState = new ImageState(newImage, NOT_HIGHLIGHTED);
        imageStateStack.push(newImageState);
    }

    public boolean isImageHighlighted() {
        return getImageState().isHighlighted();
    }

    public Image getImage() {return getImageState().image;}

    public int getHighlightedColumnIndex() {return getImageState().highlightedColumnIndex;}

    private ImageState getImageState() {return imageStateStack.peek();}

    public void highlightBluestColumn() {
        int bluestColumnIndex = getBluestColumnIndex();
        highlightColumn(bluestColumnIndex, BLUE);
    }

    public void highlightRandomColumn() {
        Random random = new Random();
        Image image = getImage();
        int width = image.getWidth();
        int randomColumnIndex = random.nextInt(width);
        highlightColumn(randomColumnIndex, RED);
    }

    public void highlightColumn(int x, Color color) {
        Image image = getImage();
        int height = image.getHeight();
        List<List<Color>> columns = image.getColumns();
        List<Color> highlightedColumn = initializeList(height, i -> color);
        columns.set(x, highlightedColumn);
        Image highlightedImage = Image.fromColumns(columns);
        ImageState newImageState = new ImageState(highlightedImage, x);
        imageStateStack.push(newImageState);
    }

    public void undo() {
        assert isImageEdited();
        // we want to revert to the last un-highlighted image.
        // if the current image is highlighted, the image before was un-highlighted.
        // thus we should undo once.
        // if the current image is un-highlighted, the image before was highlighted.
        // if the image before was highlighted, the image twice before was un-highlighted.
        // thus we should undo twice.
        if (isImageHighlighted()) {
            imageStateStack.pop();
        } else {
            imageStateStack.pop();
            imageStateStack.pop();
        }
    }

    public int getBluestColumnIndex() {
        Image image = getImage();
        List<List<Color>> columns = image.getColumns();
        List<List<Integer>> blueComponents = map2D(columns, Color::getBlue);
        List<Integer> blueComponentSums = map(blueComponents, CollectionUtilities::sum);
        int maxBlueComponentSum = Collections.max(blueComponentSums);
        return blueComponentSums.indexOf(maxBlueComponentSum);
    }

    public boolean isImageEdited() {
        return imageStateStack.size() > 1;
    }
}