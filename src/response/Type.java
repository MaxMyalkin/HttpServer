package response;

import java.util.HashMap;

public class Type {
    private static final HashMap<String, String> suffixes = new HashMap<>();

    static {
        suffixes.put(".jpg", "image/jpeg");
        suffixes.put(".jpeg", "image/jpeg");
        suffixes.put(".html", "text/html");
        suffixes.put(".js", "application/javascript");
        suffixes.put(".css", "text/css");
        suffixes.put(".png", "image/png");
        suffixes.put(".gif", "image/gif");
        suffixes.put(".swf", "application/x-shockwave-flash");
    }

    public static String get(String suffix) {
        return suffixes.get(suffix);
    }

}
