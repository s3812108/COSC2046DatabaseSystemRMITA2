// Gupta, A. N. (2019). Java Node Example. https://examples.javacodegeeks.com/java-node-example/#:~:text=Applications%20of%20Node%20class&text=Java%20Node%20class%20is%20actually,any%20non%2Dsequential%20Data%20structure. //
class IndexNode {
    private IndexAndDataStored data;
    private BlockOfIndexNodes left, right;
    private BlockOfRecordNodes leftBlock, rightBlock;

    public IndexNode() {
        data = new IndexAndDataStored();
        left = null;
        right = null;
    }

    // Constructor for index node that are NOT 1 level above the root of the tree
    public IndexNode(IndexAndDataStored data, BlockOfIndexNodes left, BlockOfIndexNodes right) {
        this.data = data;
        this.left = left;
        this.right = right;
        this.leftBlock = null;
        this.rightBlock = null;
    }

    // Constructor for index node that are 1 level above the root of the tree
    public IndexNode(IndexAndDataStored data, BlockOfRecordNodes leftBlock, BlockOfRecordNodes rightBlock) {
        this.data = data;
        this.leftBlock = leftBlock;
        this.rightBlock = rightBlock;
        this.left = null;
        this.right = null;
    }

    // Setter methods
    public void setRightPointer(BlockOfIndexNodes right) {
        this.right = right;
    }

    public void setLeftPointer(BlockOfIndexNodes left) {
        this.left = left;
    }

    public void setRightPointer(BlockOfRecordNodes rightBlock) {
        this.rightBlock = rightBlock;
    }

    public void setLeftPointer(BlockOfRecordNodes leftBlock) {
        this.leftBlock = leftBlock;
    }

    public void setData(IndexAndDataStored data) {
        this.data = data;
    }

    // Getter methods
    public BlockOfIndexNodes getLeft() {
        return left;
    }

    public BlockOfIndexNodes getRight() {
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

    // Printing string for better debugging
    public String toStringWithIndexNode() {
        String stringToBeReturned = "[ LeftPointer:" + left.toString() + ", Index:" + data.toString() + ", RightPointer:" + right.toString() + "]";
        return stringToBeReturned;
    }
    public String toStringJustIndex() {
        String stringToBeReturned = "[ Index:" + data.toString() + "]";
        return stringToBeReturned;
    }
    public String toStringWithBlock() {
        String stringToBeReturned = "[ LeftPointer:" + leftBlock.toString() + ", Index:" + data.toString() + ", RightPointer:" + rightBlock.toString() + "]";
        return stringToBeReturned;
    }
}