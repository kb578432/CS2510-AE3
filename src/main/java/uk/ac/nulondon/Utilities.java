package uk.ac.nulondon;

public class Utilities {
    /**
     * returns `value` if `value` is in the range [min, max]<p>
     * returns `min` if `value` < `min`<p>
     * returns `max` if `value` > `max`
     */
    public static int clamp(int value, int min, int max) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }
}
