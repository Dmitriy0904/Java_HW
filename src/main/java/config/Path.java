package config;

public enum Path {
    CSV_PATH("src\\main\\resources\\students.csv");

    private final String path;

    Path(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
