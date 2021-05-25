import java.util.ArrayList;

class IndexAndDataStored implements Comparable<IndexAndDataStored> {
    String index;
    ArrayList<String> dataStored;

    public IndexAndDataStored() {
    }

    // Constructor
    public IndexAndDataStored(String index, ArrayList<String> dataStored) {
        this.index = index;
        this.dataStored = dataStored;
    }

    @Override
    public int compareTo(IndexAndDataStored otherIndexAndDataStored) {
        return this.index.compareTo(otherIndexAndDataStored.index);
    }

    @Override
    public String toString() {
        return "(Index: " + index + "; DataStored: " + dataStored + ")";
    }

    // Getter methods //
    public String getIndex() {
        return index;
    }
    public ArrayList<String> getDataStored() {
        return dataStored;
    }
}