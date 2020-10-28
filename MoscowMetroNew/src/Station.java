import java.util.Objects;

public class Station {
    private String name;
    private Line line;
    private boolean isClosed;

    public Station(String name, Line line, boolean isClosed) {
        this.name = name;
        this.line = line;
        this.isClosed = isClosed;
    }

    public String getName() {
        return name;
    }

    public Line getLine() {
        return line;
    }

    public boolean isClosed() {
        return isClosed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Station station = (Station) o;
        return Objects.equals(name, station.name) &&
                Objects.equals(line, station.line);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, line);
    }

    @Override
    public String toString() {
        return "Station{" +
                "name='" + name + '\'' +
                ", line=" + line +
                ", isClosed=" + isClosed +
                '}';
    }

}
