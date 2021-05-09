import java.util.ArrayList;

public class bplustree {
    public static void main(String[] args) {
        BlockOfRecordNodes firstBlock = new BlockOfRecordNodes(3);
        RecordNode recordNodeA = new RecordNode();
        RecordNode recordNodeB = new RecordNode();
        RecordNode recordNodeC = new RecordNode();
        IndexAndDataStored recordA = new IndexAndDataStored("A", "Rest of Data");
        IndexAndDataStored recordB = new IndexAndDataStored("B", "Rest of Data");
        IndexAndDataStored recordC = new IndexAndDataStored("C", "Rest of Data");
        recordNodeA.setData(recordA);
        recordNodeB.setData(recordB);
        recordNodeC.setData(recordC);
        String testing = "B";
        System.out.println("RESIULT " + testing.compareTo("A"));
        System.out.println("firstBlock.hasRoom() " + firstBlock.hasRoom());
        firstBlock.addNode(recordNodeB);
        System.out.println(firstBlock.toString());
        System.out.println("firstBlock.hasRoom() " + firstBlock.hasRoom());
        firstBlock.addNode(recordNodeA);
        System.out.println(firstBlock.toString());
        System.out.println("firstBlock.hasRoom() " + firstBlock.hasRoom());
        firstBlock.addNode(recordNodeC);
        System.out.println(firstBlock.toString());
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

class BlockOfRecordNodes {
    private int maximumSize;
    private ArrayList<RecordNode> blockData = new ArrayList<>(maximumSize);

    public BlockOfRecordNodes(int maximumSize) {
        this.maximumSize = maximumSize;
    }

    public void addNode(RecordNode recordNode) {
        if (this.hasRoom()) {
            if (blockData.size() == 0) {
                blockData.add(recordNode);
            } else {
                System.out.println("blockdata size" + blockData.size());
                int indexLocation = blockData.size();
                for (int i = 0; i < blockData.size(); i++) {
                    System.out.println(blockData.get(i).getData().getIndex() + " compared to " + recordNode.getData().getIndex() + ": " + blockData.get(i).getData().compareTo(recordNode.getData()));
                    if (blockData.get(i).getData().compareTo(recordNode.getData()) > 0) {
                        indexLocation = i;
                        break;
                    }
                }
                System.out.println("HOHO " + (blockData.size()));
                System.out.println("indexlocation: " + indexLocation + " recordNode: " + recordNode.getData().getIndex());
                blockData.add(indexLocation, recordNode);
            }
        } else {
            System.out.println("Block is full");
        }
    }

    public Boolean hasRoom() {
        if (blockData.size() < maximumSize) {
            return true;
        } else {
            return false;
        }
    }

    // Getter methods
    public int getMaximumSize() {
        return maximumSize;
    }

    public ArrayList<RecordNode> getBlockOfRecordNodes() {
        return blockData;
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

}

// Reference from https://examples.javacodegeeks.com/java-node-example/#:~:text=Applications%20of%20Node%20class&text=Java%20Node%20class%20is%20actually,any%20non%2Dsequential%20Data%20structure. //
//class TwoPointersNode {
//    private IndexAndDataStored data;
//    private TwoPointersNode left, right;
//
//    public TwoPointersNode(){
//        data = new IndexAndDataStored();
//        left = null;
//        right = null;
//    }
//
//    public TwoPointersNode(IndexAndDataStored data, TwoPointersNode left, TwoPointersNode right) {
//        this.data = data;
//        this.left = left;
//        this.right = right;
//    }
//
//    public void setRightPointer(TwoPointersNode right){
//        this.right = right;
//    }
//
//    public void setLeftPointer(TwoPointersNode left){
//        this.left = left;
//    }
//
//    public TwoPointersNode getLeft(){
//        return left;
//    }
//
//    public TwoPointersNode getRight(){
//        return right;
//    }
//
//    public void setData(IndexAndDataStored data){
//        this.data = data;
//    }
//
//    public IndexAndDataStored getData(){
//        return data;
//    }
//
//}



