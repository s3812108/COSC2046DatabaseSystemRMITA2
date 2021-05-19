import java.util.ArrayList;

class BlockOfIndexNodes {
    private ArrayList<IndexNode> indexData = new ArrayList<>();
    private BlockOfIndexNodes neighbourRight = null;
    private BlockOfIndexNodes upperBlock = null;
    private ArrayList<BlockOfIndexNodes> children = null;

    public BlockOfIndexNodes() {
    }

    public void addNode(IndexNode indexNode) {
        if (indexData.size() == 0) {
            indexData.add(indexNode);
        } else {
            int indexLocation = indexData.size();
            for (int i = 0; i < indexData.size(); i++) {
                //System.out.println(blockData.get(i).getData().getIndex() + " compared to " + recordNode.getData().getIndex() + ": " + blockData.get(i).getData().compareTo(recordNode.getData()));
                if (indexData.get(i).getData().compareTo(indexNode.getData()) > 0) {
                    indexLocation = i;
                    break;
                }
            }
            indexData.add(indexLocation, indexNode);
        }
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

    public void setNeighbourRight(BlockOfIndexNodes neighbourRight) {
        this.neighbourRight = neighbourRight;
    }

    public BlockOfIndexNodes getUpperBlock() {
        return upperBlock;
    }

    public void setUpperBlock(BlockOfIndexNodes upperBlock) {
        this.upperBlock = upperBlock;
    }


    @Override
    public String toString() {
        String blocksToString = "[";
        for (int i = 0; i < indexData.size(); i++) {
            blocksToString += indexData.get(i).getData().toString() + " ";
        }
        blocksToString += "]";
        return blocksToString;
    }

    public int size() {
        return indexData.size();
    }

    public ArrayList<BlockOfIndexNodes> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<BlockOfIndexNodes> children) {
        this.children = children;
    }
}