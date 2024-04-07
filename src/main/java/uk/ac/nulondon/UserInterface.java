package uk.ac.nulondon;

import java.util.List;
import java.util.Scanner;

import static uk.ac.nulondon.DiskUtilities.fileExists;
import static uk.ac.nulondon.CollectionUtilities.find;

public class UserInterface {
    private final static Scanner SCANNER = new Scanner(System.in);

    private static void printDivider(int dividerWidth, char character) {
        String characterAsString = String.valueOf(character);
        String divider = characterAsString.repeat(dividerWidth);
        System.out.println(divider);
    }

    private static void printDivider() {
        printDivider(80, '-');
    }

    private static void printMenuOptions(List<MenuOption> menuOptions) {
        menuOptions.forEach(System.out::println);
        System.out.println("enter the hotkey of the option you want to choose: ");
    }

    public static MenuOption askUserForMenuOption(List<MenuOption> menuOptions) {
        printMenuOptions(menuOptions);

        String input = SCANNER.nextLine();
        MenuOption chosenMenuOption = find(
                menuOptions,
                menuOption -> menuOption.hotKey.equalsIgnoreCase(input)
        );
        boolean isInputValid = chosenMenuOption != null;
        if (!isInputValid) {
            System.out.println("input is not equal to any of the menu option hotkeys");
            printDivider();
            return askUserForMenuOption(menuOptions);
        }
        printDivider();
        return chosenMenuOption;
    }

    public static String askUserForFilePath() {
        System.out.println("enter the file path of the image you want to edit:");
        String filePath = SCANNER.nextLine();
        if (!fileExists(filePath)) {
            System.out.println("there is no file at the given file path");
            printDivider();
            return askUserForFilePath();
        }
        printDivider();
        return filePath;
    }
}
