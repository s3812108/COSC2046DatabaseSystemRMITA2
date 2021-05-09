import java.util.ArrayList;

public class bplustree {
    public static void main(String[] args) {
        Root root = new Root(2);
        BlockOfRecordNodes firstBlock = new BlockOfRecordNodes();

        RecordNode recordNodeA = new RecordNode();
        IndexAndDataStored recordA = new IndexAndDataStored("A", "Rest of Data");
        RecordNode recordNodeB = new RecordNode();
        IndexAndDataStored recordB = new IndexAndDataStored("B", "Rest of Data");
        RecordNode recordNodeC = new RecordNode();
        IndexAndDataStored recordC = new IndexAndDataStored("C", "Rest of Data");
        recordNodeA.setData(recordA);
        recordNodeB.setData(recordB);
        recordNodeC.setData(recordC);
        firstBlock.addNode(recordNodeB);
        firstBlock.addNode(recordNodeA);
        firstBlock.addNode(recordNodeC);
        System.out.println(firstBlock.toString());
        root.addBlock(firstBlock);
        root.restructure();
    }
}

class Root {
    private ArrayList<BlockOfRecordNodes> rootBlocks = new ArrayList<>();
    private ArrayList<IndexNode> index = new ArrayList<>();

    private int maximumSize;

    public Root(int maximumSize) {
        this.maximumSize = maximumSize;
    }

    public void restructure() {
        for (int i = 0; i < rootBlocks.size(); i++) {
            BlockOfRecordNodes blockSplitPart1 = new BlockOfRecordNodes();
            BlockOfRecordNodes blockSplitPart2 = new BlockOfRecordNodes();
            if (rootBlocks.get(i).size() > maximumSize) {
                String testingString1 = "Root: [";
                for (int a = 0; a < rootBlocks.size(); a++) {
                    testingString1 += rootBlocks.get(a).toString() + ", ";
                }
                testingString1 += "]";
                System.out.println("Initial: " + testingString1);
                BlockOfRecordNodes blockToBeRestructured = rootBlocks.get(i);
                ArrayList<RecordNode> blockToBeRestructuredData = blockToBeRestructured.getBlockData();
                RecordNode middleNode = blockToBeRestructuredData.get(blockToBeRestructured.size() / 2);
                for (int j = 0; j <= blockToBeRestructured.size() / 2; j++) {
                    blockSplitPart1.addNode(blockToBeRestructuredData.get(j));
                }
                blockSplitPart1.setNeighbourRight(blockSplitPart2);
                for (int k = (1 + blockToBeRestructured.size() / 2); k < blockToBeRestructured.size(); k++) {
                    blockSplitPart2.addNode(blockToBeRestructuredData.get(k));
                }
                if (index.isEmpty()) {
                    index.add(new IndexNode(middleNode.getData(), blockSplitPart1, blockSplitPart2));
                    System.out.println("middleNode" + middleNode.getData().toString());
                    String testing2 = "[";
                    System.out.println("indexsize: " + index.size());
                    for (int b = 0; b < index.size(); b++) {
                        testing2 += index.get(b).toStringWithBlock() + ", ";
                    }
                    testing2 += "]";
                    System.out.println("Index: " + testing2);
                }
                System.out.println(middleNode.getData().toString());
                System.out.println("Need restructure");
                rootBlocks.remove(i);
                rootBlocks.add(i, blockSplitPart2);
                rootBlocks.add(i, blockSplitPart1);
                String testingString = "Root: [";
                for (int a = 0; a < rootBlocks.size(); a++) {
                    testingString += rootBlocks.get(a).toString() + ", ";
                }
                testingString += "]";
                System.out.println(testingString);
            }

        }
    }

    public void addBlock(BlockOfRecordNodes newBlock) {
        this.rootBlocks.add(newBlock);
    }

    public ArrayList<BlockOfRecordNodes> getRootBlocks() {
        return rootBlocks;
    }
}

class BlockOfRecordNodes {
    private ArrayList<RecordNode> blockData = new ArrayList<>();
    private BlockOfRecordNodes neighbourRight = null;

    public BlockOfRecordNodes() {
    }

    public void addNode(RecordNode recordNode) {
        if (blockData.size() == 0) {
            blockData.add(recordNode);
        } else {
            int indexLocation = blockData.size();
            for (int i = 0; i < blockData.size(); i++) {
                //System.out.println(blockData.get(i).getData().getIndex() + " compared to " + recordNode.getData().getIndex() + ": " + blockData.get(i).getData().compareTo(recordNode.getData()));
                if (blockData.get(i).getData().compareTo(recordNode.getData()) > 0) {
                    indexLocation = i;
                    break;
                }
            }
            blockData.add(indexLocation, recordNode);
        }
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

    public void setNeighbourRight(BlockOfRecordNodes neighbourRight) {
        this.neighbourRight = neighbourRight;
    }

    @Override
    public String toString() {
        String blocksToString = "[";
        for (int i = 0; i < blockData.size(); i++) {
            blocksToString += blockData.get(i).getData().toString() + " ";
        }
        blocksToString += "]";
        return blocksToString;
    }

    public int size() {
        return blockData.size();
    }

}

class IndexAndDataStored implements Comparable<IndexAndDataStored> {
    String index;
    String dataStored;

    public IndexAndDataStored() {
    }

    // Constructor
    public IndexAndDataStored(String index, String dataStored) {
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

    public String getDataStored() {
        return dataStored;
    }
}

// Reference from https://examples.javacodegeeks.com/java-node-example/#:~:text=Applications%20of%20Node%20class&text=Java%20Node%20class%20is%20actually,any%20non%2Dsequential%20Data%20structure. //
class RecordNode {
    private IndexAndDataStored data;
    private RecordNode right;
    private ArrayList<RecordNode> rightRootRecord = new ArrayList<>();

    // Constructors //
    public RecordNode() {
        data = new IndexAndDataStored();
        right = null;
    }

    public RecordNode(IndexAndDataStored data, RecordNode right) {
        this.data = data;
        this.right = right;
    }

    public RecordNode(IndexAndDataStored data, ArrayList<RecordNode> NextRootRecords) {
        this.data = data;
        this.rightRootRecord = NextRootRecords;
        this.right = null;
    }

    // Setter methods //
    public void setRightPointer(RecordNode right) {
        this.right = right;
    }

    // Getter methods //
    public RecordNode getRight() {
        return right;
    }

    public IndexAndDataStored getData() {
        return data;
    }

    public void setData(IndexAndDataStored data) {
        this.data = data;
    }

    public ArrayList<RecordNode> getRightRootRecord() {
        return rightRootRecord;
    }

    public void setRightRootRecord(ArrayList<RecordNode> rightRootRecord) {
        this.rightRootRecord = rightRootRecord;
    }
}

// Reference from https://examples.javacodegeeks.com/java-node-example/#:~:text=Applications%20of%20Node%20class&text=Java%20Node%20class%20is%20actually,any%20non%2Dsequential%20Data%20structure. //
class IndexNode {
    private IndexAndDataStored data;
    private IndexNode left, right;
    private BlockOfRecordNodes leftBlock, rightBlock;

    public IndexNode() {
        data = new IndexAndDataStored();
        left = null;
        right = null;
    }

    public IndexNode(IndexAndDataStored data, IndexNode left, IndexNode right) {
        this.data = data;
        this.left = left;
        this.right = right;
        this.leftBlock = null;
        this.rightBlock = null;
    }

    public IndexNode(IndexAndDataStored data, BlockOfRecordNodes leftBlock, BlockOfRecordNodes rightBlock) {
        this.data = data;
        this.leftBlock = leftBlock;
        this.rightBlock = rightBlock;
        this.left = null;
        this.right = null;
    }

    // Setter methods
    public void setRightPointer(IndexNode right) {
        this.right = right;
    }

    public void setLeftPointer(IndexNode left) {
        this.left = left;
    }

    public void setRightPointer(BlockOfRecordNodes rightBlock) {
        this.rightBlock = rightBlock;
    }

    public void setLeftPointer(BlockOfRecordNodes leftBlock) {
        this.leftBlock = leftBlock;
    }

    // Getter methods
    public IndexNode getLeft() {
        return left;
    }

    public IndexNode getRight() {
        return right;
    }

    public BlockOfRecordNodes getLeftBlock() {
        return leftBlock;
    }

    public BlockOfRecordNodes getRightBlock() {
        return rightBlock;
    }

    public IndexAndDataStored getData() {
        return data;
    }

    public void setData(IndexAndDataStored data) {
        this.data = data;
    }

    public String toStringWithIndexNode() {
        String stringToBeReturned = "[ LeftPointer:" + left.toString() + ", Index:" + data.toString() + ", RightPointer:" + right.toString() + "]";
        return stringToBeReturned;
    }

    public String toStringWithBlock() {
        String stringToBeReturned = "[ LeftPointer:" + leftBlock.toString() + ", Index:" + data.toString() + ", RightPointer:" + rightBlock.toString() + "]";
        return stringToBeReturned;
    }
}



