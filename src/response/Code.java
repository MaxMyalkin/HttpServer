package response;


public class Code {
    private int numeric;
    private String transcript;

    public Code(int code) {
        numeric = code;
        switch (code) {
            case 200:
                transcript = "OK";
                break;
            case 403:
                transcript = "FORBIDDEN";
                break;
            case 404:
                transcript = "NOT FOUND";
                break;
            case 405:
                transcript = "METHOD NOT ALLOWED";
                break;
        }
    }

    public int getNumeric() {
        return numeric;
    }

    public String getTranscript() {
        return transcript;
    }
}
