import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ColorGenerator {

    private static final Random random = new Random();
    private static final Map<String, Boolean> colorMap = new HashMap<>();

    public static String generateRandomHexColor() {
        String color;
        do {
            int r = random.nextInt(256);
            int g = random.nextInt(256);
            int b = random.nextInt(256);
            color = String.format("#%02X%02X%02X", r, g, b);
        } while (colorMap.containsKey(color));
        colorMap.put(color, true);
        return color;
    }
}