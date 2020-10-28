import java.util.Comparator;

public class StationLineComparator implements Comparator<Station> {

    @Override
    public int compare(Station o1, Station o2) {
        return o1.getLine().getNumber().compareTo(o2.getLine().getNumber());
    }
}
