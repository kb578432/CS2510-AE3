package uk.ac.nulondon;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.awt.Color;
import java.util.List;
import java.util.stream.Stream;

import static java.awt.Color.*;
import static org.assertj.core.api.Assertions.assertThat;
import static uk.ac.nulondon.CollectionUtilities.map;

class TestImageEditor {
    @ParameterizedTest
    @MethodSource("deleteHighlightedColumnArguments")
    void deleteHighlightedColumn(Image image, int x, Image expectedImage) {
        ImageEditor imageEditor = new ImageEditor(image);
        imageEditor.highlightColumn(x, GREEN);
        imageEditor.deleteHighlightedColumn();
        Image actualImage = imageEditor.getImage();
        assertThat(actualImage).isEqualTo(expectedImage);
    }

    public static Stream<Arguments> deleteHighlightedColumnArguments() {
        Image image = Images.getImage(0);
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
    @MethodSource("getBluestColumnIndexArguments")
    void getBluestColumnIndex(Image image, int expectedBluestColumnIndex) {
        ImageEditor imageEditor = new ImageEditor(image);
        int actualBluestColumnIndex = imageEditor.getBluestColumnIndex();
        assertThat(actualBluestColumnIndex).isEqualTo(expectedBluestColumnIndex);
    }

    public static Stream<Arguments> getBluestColumnIndexArguments() {
        return Stream.of(
                Arguments.of(Images.getImage(0), 1),
                Arguments.of(Images.getImage(1), 2),
                Arguments.of(Images.getImage(2), 0),
                Arguments.of(Images.getImage(3), 2),
                Arguments.of(Images.getImage(4), 0),
                Arguments.of(Images.getImage(5), 1)
        );
    }

    @ParameterizedTest
    @MethodSource("getImageStream")
    void getHighlightedColumnIndex(Image image) {
        ImageEditor imageEditor = new ImageEditor(image);
        int bluestColumnIndex = imageEditor.getBluestColumnIndex();
        imageEditor.highlightBluestColumn();
        int highlightedColumnIndex = imageEditor.getHighlightedColumnIndex();
        assertThat(highlightedColumnIndex).isEqualTo(bluestColumnIndex);
    }

    @ParameterizedTest
    @MethodSource("getImageStream")
    void getImage(Image image) {
        ImageEditor imageEditor = new ImageEditor(image);
        assertThat(imageEditor.getImage()).isEqualTo(image);
    }

    public static Stream<Image> getImageStream() {
        return map(Images.imagesAsRows, Image::fromRows).stream();
    }

    @ParameterizedTest
    @MethodSource("highlightBluestColumnArguments")
    void highlightBluestColumn(Image image, Image expectedImage) {
        ImageEditor imageEditor = new ImageEditor(image);
        imageEditor.highlightBluestColumn();
        Image actualImage = imageEditor.getImage();
        assertThat(actualImage).isEqualTo(expectedImage);
    }

    public static Stream<Arguments> highlightBluestColumnArguments() {
        return Stream.of(
                Arguments.of(
                        Images.getImage(0),
                        Image.fromRows(
                                List.of(
                                        List.of(BLUE, BLUE, RED),
                                        List.of(RED, BLUE, RED),
                                        List.of(RED, BLUE, RED)
                                )
                        )
                ),
                Arguments.of(
                        Images.getImage(1),
                        Image.fromRows(
                                List.of(
                                        List.of(BLUE, RED, BLUE),
                                        List.of(RED, RED, BLUE),
                                        List.of(RED, RED, BLUE)
                                )
                        )
                ),
                Arguments.of(
                        Images.getImage(2),
                        Image.fromRows(
                                List.of(
                                        List.of(BLUE, BLUE, RED),
                                        List.of(BLUE, RED, RED),
                                        List.of(BLUE, RED, RED)
                                )
                        )
                )
        );
    }

    @ParameterizedTest
    @MethodSource("highlightColumnArguments")
    void highlightColumn(Image image, int x, Color color, Image expectedImage) {
        ImageEditor imageEditor = new ImageEditor(image);
        imageEditor.highlightColumn(x, color);
        Image actualImage = imageEditor.getImage();
        assertThat(actualImage).isEqualTo(expectedImage);
    }

    public static Stream<Arguments> highlightColumnArguments() {
        Image image = Images.getImage(0);
        return Stream.of(
                Arguments.of(
                        image, 0, GREEN,
                        Image.fromRows(
                                List.of(
                                        List.of(GREEN, BLUE, RED),
                                        List.of(GREEN, BLUE, RED),
                                        List.of(GREEN, RED, RED)
                                )
                        )
                ),
                Arguments.of(
                        image, 1, GREEN,
                        Image.fromRows(
                                List.of(
                                        List.of(BLUE, GREEN, RED),
                                        List.of(RED, GREEN, RED),
                                        List.of(RED, GREEN, RED)
                                )
                        )
                ),
                Arguments.of(
                        image, 2, GREEN,
                        Image.fromRows(
                                List.of(
                                        List.of(BLUE, BLUE, GREEN),
                                        List.of(RED, BLUE, GREEN),
                                        List.of(RED, RED, GREEN)
                                )
                        )
                )
        );
    }

    @ParameterizedTest
    @MethodSource("getImageStream")
    void isImageHighlighted(Image image) {
        ImageEditor imageEditor = new ImageEditor(image);
        assertThat(imageEditor.isImageHighlighted()).isFalse();
        imageEditor.highlightColumn(0, GREEN);
        assertThat(imageEditor.isImageHighlighted()).isTrue();
        imageEditor.undo();
        assertThat(imageEditor.isImageHighlighted()).isFalse();
    }

    @ParameterizedTest
    @MethodSource("getImageStream")
    void undo(Image image) {
        ImageEditor imageEditor = new ImageEditor(image);
        imageEditor.highlightRandomColumn();
        imageEditor.undo();
        assertThat(imageEditor.getImage()).isEqualTo(image);
    }
}