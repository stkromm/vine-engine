package vine.settings;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Steffen
 *
 */
final class ConfigSerializer {
    private ConfigSerializer() {

    }

    /**
     * @param values
     * @param path
     */
    public static void saveIniToFile(final Map<String, String> values, final String path) {
        final Path file = Paths.get(path);
        final List<String> data = new ArrayList<>();
        values.forEach((key, value) -> data.add(key + ":" + value));
        try {
            Files.write(file, data, Charset.forName("UTF-8"));
        } catch (IOException e) {
            Logger.getGlobal().log(Level.SEVERE, "Auto-generated catch block", e);
        }
    }
}