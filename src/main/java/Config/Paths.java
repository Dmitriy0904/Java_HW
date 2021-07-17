package Config;

public enum Paths {
    PROPS_PATH("/app.properties");

    private final String path;

    Paths(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
