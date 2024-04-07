package uk.ac.nulondon;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static uk.ac.nulondon.DiskUtilities.writeToDisk;
import static uk.ac.nulondon.UserInterface.askUserForMenuOption;
import static uk.ac.nulondon.UserInterface.askUserForFilePath;

public class Main {
    private final ImageEditor imageEditor;

    private class HighlightRandomColumnMenuOption extends MenuOption {
        public HighlightRandomColumnMenuOption() {
            super("highlight a random column", "r");
        }

        @Override
        public void onChosen() {
            imageEditor.highlightRandomColumn();
        }
    }

    private class HighlightBluestColumnMenuOption extends MenuOption {
        public HighlightBluestColumnMenuOption() {
            super("highlight the bluest column", "b");
        }

        @Override
        public void onChosen() {
            imageEditor.highlightBluestColumn();
        }
    }

    private class UndoMenuOption extends MenuOption {
        public UndoMenuOption() {
            super("undo", "u");
        }

        @Override
        public void onChosen() {
            imageEditor.undo();
        }
    }

    private class DeleteHighlightedColumnMenuOption extends MenuOption {
        public DeleteHighlightedColumnMenuOption() {
            super("delete the highlighted column", "d");
        }

        @Override
        public void onChosen() {
            imageEditor.deleteHighlightedColumn();
        }

    }

    private class QuitMenuOption extends MenuOption {
        public QuitMenuOption() {
            super("quit", "q");
        }

        @Override
        public void onChosen() {
            System.exit(0);
        }
    }

    // @formatter:off
    private final MenuOption highlightRandomColumnMenuOption   = new HighlightRandomColumnMenuOption();
    private final MenuOption highlightBluestColumnMenuOption   = new HighlightBluestColumnMenuOption();
    private final MenuOption undoMenuOption                    = new UndoMenuOption();
    private final MenuOption deleteHighlightedColumnMenuOption = new DeleteHighlightedColumnMenuOption();
    private final MenuOption quitMenuOption                    = new QuitMenuOption();
    // @formatter:on

    private Main() throws IOException {
        String filePath = askUserForFilePath();
        Image image = Image.fromFilePath(filePath);
        imageEditor = new ImageEditor(image);
        for (int i = 0; ; i++) {
            filePath = "src/main/resources/image-editor/image-" + i + ".png";
            image = imageEditor.getImage();
            BufferedImage bufferedImage = image.getBufferedImage();
            writeToDisk(bufferedImage, filePath);

            List<MenuOption> menuOptions = getMenuOptions();
            MenuOption chosenMenuOption = askUserForMenuOption(menuOptions);
            chosenMenuOption.onChosen();
        }
    }

    private List<MenuOption> getMenuOptions() {
        List<MenuOption> menuOptions = new ArrayList<>();
        Image image = imageEditor.getImage();
        boolean isImageMinimumSize = image.getWidth() == 1;

        if (imageEditor.isImageHighlighted() && !isImageMinimumSize) {
            menuOptions.add(deleteHighlightedColumnMenuOption);

        } else {
            menuOptions.add(highlightBluestColumnMenuOption);
            menuOptions.add(highlightRandomColumnMenuOption);
        }
        if (imageEditor.isImageEdited()) {
            menuOptions.add(undoMenuOption);
        }
        menuOptions.add(quitMenuOption);
        return menuOptions;
    }

    public static void main(String[] args) throws IOException {
        new Main();
    }
}
