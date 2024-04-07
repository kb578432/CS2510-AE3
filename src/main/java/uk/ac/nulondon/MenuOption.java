package uk.ac.nulondon;

public abstract class MenuOption {
    public final String description;
    public final String hotKey;

    public MenuOption(String description, String hotKey) {
        this.description = description;
        this.hotKey = hotKey;
    }

    /**
     * the method to invoke upon this menu option being chosen.
     */
    public abstract void onChosen();

    @Override
    public String toString() {
        return String.format("%s: %s", hotKey, description);
    }
}
