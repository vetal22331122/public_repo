import java.util.Objects;
import java.util.TreeSet;

public class Connection {
    private TreeSet<Station> connectedStations;
    private boolean isActive;

    public Connection(TreeSet<Station> connectedStations) {
        this.connectedStations = connectedStations;
        int i = 0;
        for (Station station : connectedStations) {
            if(station.isClosed()) {
                i++;
            }
        }
        if(connectedStations.size() - i < 2) {
            this.isActive = false;
        } else {
            this.isActive = true;
        }
    }

    public TreeSet<Station> getConnectedStations() {
        return connectedStations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Connection that = (Connection) o;
        return Objects.equals(connectedStations, that.connectedStations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(connectedStations);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Station station : connectedStations) {
            builder.append(station.getName() + "(" + station.getLine().getNumber() + ") - ");
        }
        builder.append("\n");
        return builder.toString();
    }
}
