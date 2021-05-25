import java.util.ArrayList;

class BlockOfRecordNodes {
    private ArrayList<RecordNode> blockData = new ArrayList<>();
    private BlockOfRecordNodes neighbourRight = null;
    private BlockOfIndexNodes parentIndexBlock = null;

    public BlockOfRecordNodes() {
    }

    // Function to add a record node into the block of records
    public void addNode(RecordNode recordNode) {
        if (blockData.size() == 0) {
            blockData.add(recordNode);
        } else {
            int indexLocation = blockData.size();
            for (int i = 0; i < blockData.size(); i++) {
                if (blockData.get(i).getData().compareTo(recordNode.getData()) > 0) {
                    indexLocation = i;
                    break;
                }
            }
            blockData.add(indexLocation, recordNode);
        }
    }

    // Returning the size of the block of record nodes
    public int size() {
        return blockData.size();
    }

    public Boolean hasRoom(int maximumSize) {
        if (blockData.size() < maximumSize) {
            return true;
        } else {
            return false;
        }
    }

    // Getter methods
    public ArrayList<RecordNode> getBlockData() {
        return blockData;
    }

    public BlockOfRecordNodes getNeighbourRight() {
        return neighbourRight;
    }

    public BlockOfIndexNodes getParentIndexBlock() {
        return parentIndexBlock;
    }

    // Setter methods
    public void setNeighbourRight(BlockOfRecordNodes neighbourRight) {
        this.neighbourRight = neighbourRight;
    }

    public void setParentIndexBlock(BlockOfIndexNodes parentIndexBlock) {
        this.parentIndexBlock = parentIndexBlock;
    }

    // String format adjusted for debugging
    @Override
    public String toString() {
        String blocksToString = "[";
        for (int i = 0; i < blockData.size(); i++) {
            blocksToString += blockData.get(i).getData().toString() + " ";
        }
        blocksToString += "]";
        return blocksToString;
    }
}
