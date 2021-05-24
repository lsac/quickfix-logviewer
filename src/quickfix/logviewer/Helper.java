package quickfix.logviewer;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

public class Helper {
    private Helper(){}
    public static File getFileFromResourceAsStream(Object obj, String fileName) throws URISyntaxException {

        ClassLoader classLoader = obj.getClass().getClassLoader();

        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        }

        //File file = new File(resource.getFile());
        File file = new File(resource.toURI());
        return file;
    }
}
