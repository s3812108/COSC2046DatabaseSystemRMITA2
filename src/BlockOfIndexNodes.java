import java.util.ArrayList;

class BlockOfIndexNodes {
    private ArrayList<IndexNode> indexData = new ArrayList<>();
    private BlockOfIndexNodes neighbourRight = null;
    private BlockOfIndexNodes upperBlock = null;

    public BlockOfIndexNodes() {
    }

    // Function to add an index node into the block of indexes
    public void addNode(IndexNode indexNode) {
        if (indexData.size() == 0) {
            indexData.add(indexNode);
        } else {
            int indexLocation = indexData.size();
            for (int i = 0; i < indexData.size(); i++) {
                if (indexData.get(i).getData().compareTo(indexNode.getData()) > 0) {
                    indexLocation = i;
                    break;
                }
            }
            indexData.add(indexLocation, indexNode);
        }
    }

    // Returning the size of the block of index nodes
    public int size() {
        return indexData.size();
    }

    public Boolean hasRoom(int maximumSize) {
        if (indexData.size() < maximumSize) {
            return true;
        } else {
            return false;
        }
    }

    // Getter methods
    public ArrayList<IndexNode> getIndexData() {
        return indexData;
    }

    public BlockOfIndexNodes getNeighbourRight() {
        return neighbourRight;
    }

    public BlockOfIndexNodes getUpperBlock() {
        return upperBlock;
    }

    // Setter methods
    public void setNeighbourRight(BlockOfIndexNodes neighbourRight) {
        this.neighbourRight = neighbourRight;
    }

    public void setUpperBlock(BlockOfIndexNodes upperBlock) {
        this.upperBlock = upperBlock;
    }


    // String format for appropriate debugging
    @Override
    public String toString() {
        String blocksToString = "[";
        for (int i = 0; i < indexData.size(); i++) {
            blocksToString += indexData.get(i).getData().toString() + " ";
        }
        blocksToString += "] ******** ";
        return blocksToString;
    }
}