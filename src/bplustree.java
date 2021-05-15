import java.util.ArrayList;

public class bplustree {
    public static void main(String[] args) {
        Root root = new Root(2);
        BlockOfRecordNodes firstBlock = new BlockOfRecordNodes();

        RecordNode recordNodeA = new RecordNode();
        IndexAndDataStored recordA = new IndexAndDataStored("A", "Data content A");
        recordNodeA.setData(recordA);
        RecordNode recordNodeB = new RecordNode();
        IndexAndDataStored recordB = new IndexAndDataStored("B", "Data content B");
        recordNodeB.setData(recordB);
        RecordNode recordNodeC = new RecordNode();
        IndexAndDataStored recordC = new IndexAndDataStored("C", "Data content C");
        recordNodeC.setData(recordC);
        RecordNode recordNodeD = new RecordNode();
        IndexAndDataStored recordD = new IndexAndDataStored("D", "Data content D");
        recordNodeD.setData(recordD);
        RecordNode recordNodeE = new RecordNode();
        IndexAndDataStored recordE = new IndexAndDataStored("E", "Data content E");
        recordNodeE.setData(recordE);
        RecordNode recordNodeF = new RecordNode();
        IndexAndDataStored recordF = new IndexAndDataStored("F", "Data content F");
        recordNodeF.setData(recordF);
        RecordNode recordNodeG = new RecordNode();
        IndexAndDataStored recordG = new IndexAndDataStored("G", "Data content G");
        recordNodeG.setData(recordG);
        root.addBlock(firstBlock);
        root.addNode(recordNodeB);
        root.addNode(recordNodeF);
        root.addNode(recordNodeA);
        root.addNode(recordNodeC);
        root.addNode(recordNodeE);
        root.addNode(recordNodeD);
        root.addNode(recordNodeG);
//        firstBlock.addNode(recordNodeA);
//        firstBlock.addNode(recordNodeC);
//        firstBlock.addNode(recordNodeD);
//        System.out.println(firstBlock.toString());
        System.out.println(root.toString());

    }
}

class Root {
    private ArrayList<BlockOfRecordNodes> rootBlocks = new ArrayList<>();
    private ArrayList<BlockOfIndexNodes> index = new ArrayList<>();

    public void addNode(RecordNode rn) {
        System.out.println(rn.getData().toString());
        BlockOfRecordNodes selectedBlock = this.searchWhichBlock(rn);
        selectedBlock.addNode(rn);
        this.restructure();
    }

    public BlockOfRecordNodes searchWhichBlock(RecordNode rn) {
        String rnIndex = rn.getData().getIndex();
        if (index.isEmpty()) {
            return rootBlocks.get(0);
        } else {
            System.out.println("Record Node to Search " + rn.getData().getIndex().toString());
            System.out.println("THIS LEADS TO" + traverse(rnIndex, index.get(0).getIndexData().get(0)));
            System.out.println("Block belongs to: " + traverse(rnIndex, index.get(0).getIndexData().get(0)).toString());
            IndexNode startTraversingFromThisIndex = null;
            BlockOfIndexNodes highestBlockOfIndex = index.get(0);
            int startAtIndexNode = highestBlockOfIndex.size() - 1;
            for (int i = 0; i < highestBlockOfIndex.size(); i++) {
                String indexOfIndexNodeInHighestBlock = highestBlockOfIndex.getIndexData().get(i).getData().getIndex();
                if (indexOfIndexNodeInHighestBlock.compareTo(rnIndex) >= 0) {
                    startAtIndexNode = i;
                    break;
                }
            }
            startTraversingFromThisIndex = highestBlockOfIndex.getIndexData().get(startAtIndexNode);
            return traverse(rnIndex, startTraversingFromThisIndex);
        }
    }

    public BlockOfIndexNodes searchWhichIndexBlockLastVisit(RecordNode rn) {
        String rnIndex1 = rn.getData().getIndex();
        if (index.isEmpty()) {
            return index.get(0);
        } else {
            IndexNode startTraversingFromThisIndex = null;
            BlockOfIndexNodes highestBlockOfIndex = index.get(0);
            int startAtIndexNode = highestBlockOfIndex.size() - 1;
            for (int i = 0; i < highestBlockOfIndex.size(); i++) {
                String indexOfIndexNodeInHighestBlock = highestBlockOfIndex.getIndexData().get(i).getData().getIndex();
                if (indexOfIndexNodeInHighestBlock.compareTo(rnIndex1) >= 0) {
                    startAtIndexNode = i;
                    break;
                }
            }
            startTraversingFromThisIndex = highestBlockOfIndex.getIndexData().get(startAtIndexNode);
            return traverseTillLastIndexBlock(rnIndex1, startTraversingFromThisIndex, highestBlockOfIndex);
        }
    }

    // reference https://stackoverflow.com/questions/15306452/traversing-through-all-nodes-of-a-binary-tree-in-java //
    public BlockOfRecordNodes traverse(String rnIndex, IndexNode root) {
        System.out.println("RnIndex is " + rnIndex + " root is " + root.getData().toString());
        String rootIndex = root.getData().getIndex();
        BlockOfRecordNodes blockToReturn = null;
        System.out.println("***************** RECURSION BEGINS ***********************");
        // If the index to be searched is less or equal to than the indexNode being searched for, go to its left pointer of blockofindexnodes //
        if ((rnIndex.compareTo(rootIndex) <= 0) && (root.getLeft() != null) && (root.getLeftBlock() == null)) {
            BlockOfIndexNodes leftBlockIndexNodes = root.getLeft();
            for (int i = 0; i < leftBlockIndexNodes.size(); i++) {
                IndexNode selectedIndexNode = leftBlockIndexNodes.getIndexData().get(i);
                System.out.println(selectedIndexNode.getData().getIndex());
                System.out.println(selectedIndexNode.getData().getIndex().compareTo(rootIndex));
                if (selectedIndexNode.getData().getIndex().compareTo(rootIndex) >= 1) {
                    System.out.println("GOES LEFT" + selectedIndexNode);
                    blockToReturn = traverse(rnIndex, selectedIndexNode);
                }
            }
        }
        if ((rnIndex.compareTo(rootIndex) > 0) && (root.getRight() != null) && (root.getRightBlock() == null)) {
            BlockOfIndexNodes rightBlockIndexNodes = root.getRight();
            for (int i = 0; i < rightBlockIndexNodes.size(); i++) {
                IndexNode selectedIndexNode = rightBlockIndexNodes.getIndexData().get(i);
                if (selectedIndexNode.getData().getIndex().compareTo(rootIndex) >= 1) {
                    System.out.println("GOES RIGHT" + selectedIndexNode.getData().toString());
                    System.out.println("SHOULD NOT GO BACK TO TRAVERSE BOTTOM");
                    System.out.println("GOES RIGHT" + selectedIndexNode.getData().toString());
                    System.out.println("SHOULD NOT GO BACK TO TRAVERSE BOTTOM");
                    System.out.println("HERE IS THE RESULT: " + traverse(rnIndex, selectedIndexNode));
                    blockToReturn = traverse(rnIndex, selectedIndexNode);
                }
            }
        }
        if ((rnIndex.compareTo(rootIndex) <= 0) && (root.getLeft() == null) && (root.getLeftBlock() != null)) {
            System.out.println("Reached return 1");
            return root.getLeftBlock();
        }
        int wierd = 0;
        if ((rnIndex.compareTo(rootIndex) > 0) && (root.getRight() == null) && (root.getRightBlock() != null)) {
            System.out.println("Reached return 2");
            wierd = 1;
            System.out.println("Weird changes to " + wierd);
            System.out.println("ROOT.GETRIGHTBLOCK" + root.getRightBlock());
            blockToReturn = root.getRightBlock();
            System.out.println("SHOULD NOT GO BACK TO TRAVERSE BOTTOM1");
            return root.getRightBlock();
        }
        System.out.println("Weird " + wierd);
        System.out.println("Current RnIndex is " + rnIndex + " Current root is " + root.getData().toString());
        System.out.println("why");
        System.out.println("Passing on the second argument? " + ((rnIndex.compareTo(rootIndex) > 0) && (root.getRight() != null) && (root.getRightBlock() == null)));
        System.out.println("WHY IT KEEPS ON SKIPPING THE FIRST FOUND RETURN?");
        return blockToReturn;


        //        if ((rnIndex.compareTo(rootIndex) <=0) && (root.getLeft() == null) && (root.getLeftBlock() == null)){
//            System.out.println("REACHED LEFT");
//            BlockOfRecordNodes newBlockOnLeft = new BlockOfRecordNodes();
//            root.setLeftPointer(newBlockOnLeft);
//            return newBlockOnLeft;
//        }
//        if ((rnIndex.compareTo(rootIndex) > 0) && (root.getRight() == null) && (root.getRightBlock() == null)){
//            System.out.println("REACHED RIGHT");
//            BlockOfRecordNodes newBlockOnRight = new BlockOfRecordNodes();
//            root.setLeftPointer(newBlockOnRight);
//            return newBlockOnRight;
//        }
    }

    // reference https://stackoverflow.com/questions/15306452/traversing-through-all-nodes-of-a-binary-tree-in-java //
    public BlockOfIndexNodes traverseTillLastIndexBlock(String rnIndex, IndexNode root, BlockOfIndexNodes currentlyIn) {
        String rootIndex = root.getData().getIndex();
        BlockOfIndexNodes blockOfIndexNodesToReturn = null;
        BlockOfIndexNodes leftBlockIndexNodes = root.getLeft();
        BlockOfIndexNodes rightBlockIndexNodes = root.getRight();
        if ((rnIndex.compareTo(rootIndex) <= 0) && (root.getLeft() != null) && (root.getLeftBlock() == null)) {
            int counter = leftBlockIndexNodes.size();
            for (int i = 0; i < leftBlockIndexNodes.size(); i++) {
                IndexNode selectedIndexNode = leftBlockIndexNodes.getIndexData().get(i);
                System.out.println(selectedIndexNode.getData().getIndex().compareTo(rootIndex) <= 0);
                if (selectedIndexNode.getData().getIndex().compareTo(rootIndex) <= 0) {
                    traverseTillLastIndexBlock(rnIndex, selectedIndexNode, leftBlockIndexNodes);
                    break;
                }
            }
        }
        if ((rnIndex.compareTo(rootIndex) > 0) && (root.getRight() != null) && (root.getRightBlock() == null)) {
            for (int i = 0; i < rightBlockIndexNodes.size(); i++) {
                IndexNode selectedIndexNode = rightBlockIndexNodes.getIndexData().get(i);
                if (selectedIndexNode.getData().getIndex().compareTo(rootIndex) <= 0) {
                    traverseTillLastIndexBlock(rnIndex, selectedIndexNode, leftBlockIndexNodes);
                    break;
                }
            }
        }
        if ((rnIndex.compareTo(rootIndex) <= 0) && (root.getLeft() == null) && (root.getLeftBlock() != null)) {
            System.out.println(currentlyIn.toString());
            System.out.println("Reached 3");
            return currentlyIn;
        }
        if ((rnIndex.compareTo(rootIndex) > 0) && (root.getRight() == null) && (root.getRightBlock() != null)) {
            System.out.println("REached 4");
            return currentlyIn;
        }
        System.out.println("what");
        return currentlyIn;
    }

    private int maximumSize;

    public Root(int maximumSize) {
        this.maximumSize = maximumSize;
    }

    // Restructure the tree //
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
                    BlockOfIndexNodes firstIndexBlock = new BlockOfIndexNodes();
                    IndexNode appendedIndexNode = new IndexNode(middleNode.getData(), blockSplitPart1, blockSplitPart2);
                    firstIndexBlock.addNode(appendedIndexNode);
                    blockSplitPart1.setParentIndexBlock(firstIndexBlock);
                    blockSplitPart2.setParentIndexBlock(firstIndexBlock);
                    System.out.println("blocksplitpart1 " + blockSplitPart1);
                    System.out.println("blocksplitpart2 " + blockSplitPart2);
                    index.add(firstIndexBlock);
                    System.out.println("middleNode" + middleNode.getData().toString());
                    String testing2 = "[";
                    System.out.println("indexsize: " + index.size());
                    for (int b = 0; b < index.size(); b++) {
                        testing2 += (b + 1) + " level *** " + firstIndexBlock.getIndexData().get(b).toString() + ", ";
                    }
                    testing2 += "]";
                    System.out.println("Index: " + testing2);
                } else {
                    BlockOfIndexNodes blockSplitPart1Parent = searchWhichIndexBlockLastVisit(blockSplitPart1.getBlockData().get(blockSplitPart1.size() - 1));
                    blockSplitPart1.setParentIndexBlock(blockSplitPart1Parent);
                    BlockOfIndexNodes blockSplitPart2Parent = searchWhichIndexBlockLastVisit(blockSplitPart1.getBlockData().get(blockSplitPart1.size() - 1));
                    blockSplitPart2.setParentIndexBlock(blockSplitPart2Parent);
                    System.out.println("blocksplitpart1Parent " + blockSplitPart2Parent);
                    System.out.println("blocksplitpart2Parent " + blockSplitPart2Parent);
                    if (rootBlocks.size() == 3) {
                        System.out.println("parentsblockof0 " + rootBlocks.get(0).getParentIndexBlock());
                        System.out.println("parentsblockof1 " + rootBlocks.get(1).getParentIndexBlock());
                        System.out.println("parentsblockof2 " + rootBlocks.get(2).getParentIndexBlock());
                    }

                    IndexNode startTraversingFromThisIndex = null;
                    // STUCK, HOW TO KNOW WHICH BLOCKOFINDEXNODE IN INDEX SHOULD BE PLACED AT //
                    int indexOfParentInIndexVar = index.indexOf(blockToBeRestructured.getParentIndexBlock());
                    System.out.println("blockToBeRestructured " + blockToBeRestructured);
                    System.out.println("blockToBeRestructured.getParentIndexBlock()" + blockToBeRestructured.getParentIndexBlock());
                    String testingString2 = "";
                    for (int a = 0; a < index.get(0).getIndexData().size(); a++) {
                        testingString2 += index.get(0).getIndexData().get(a).toStringWithBlock() + ", ";
                    }
                    System.out.println("indexHAAH" + testingString2);
                    BlockOfIndexNodes indexBlockLocation = index.get(indexOfParentInIndexVar);
                    IndexNode appendedIndexNode = new IndexNode(middleNode.getData(), blockSplitPart1, blockSplitPart2);
                    // Once the new index has been added, the left and right neighbouring index needs to point at the same updated record of blocks
                    indexBlockLocation.addNode(appendedIndexNode);

                    int previousIndexIndex = indexBlockLocation.getIndexData().indexOf(appendedIndexNode) - 1;
                    if (indexBlockLocation.getIndexData().size() >= 3) {
                        int rightIndex = indexBlockLocation.getIndexData().indexOf(appendedIndexNode) + 1;
                        indexBlockLocation.getIndexData().get(rightIndex).setLeftPointer(appendedIndexNode.getRightBlock());
                    }
                    indexBlockLocation.getIndexData().get(previousIndexIndex).setRightPointer(appendedIndexNode.getLeftBlock());
                    System.out.println("middleNode" + middleNode.getData().toString());
                    String testing2 = "Level 1 [ ";
                    System.out.println("indexsize: " + index.size());
                    for (int b = 0; b < indexBlockLocation.size(); b++) {
                        testing2 += indexBlockLocation.getIndexData().get(b).getData().toString() + ", ";
                    }
                    testing2 += "]";
                    System.out.println("Index: " + testing2);
                    restructureIndex();
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

    // Restructure the tree //
    public void restructureIndex() {
        for (int i = 0; i < index.size(); i++) {
            BlockOfIndexNodes blockIndexSplitPart1 = new BlockOfIndexNodes();
            BlockOfIndexNodes blockIndexSplitPart2 = new BlockOfIndexNodes();
            if (index.get(i).size() > maximumSize) {
                String testingString1 = "Index: [";
                for (int a = 0; a < index.size(); a++) {
                    testingString1 += index.get(a).toString() + ", ";
                }
                testingString1 += "]";
                System.out.println("Initial: " + testingString1);
                BlockOfIndexNodes blockOfIndexToBeRestructured = index.get(i);
                ArrayList<BlockOfRecordNodes> blockOfRecordsNodesHoldByTheBlockOfIndex = new ArrayList<>();
                boolean counterToInsertBackBlockOfRecord = false;
                for (int k = 0; k < blockOfIndexToBeRestructured.getIndexData().size(); k++) {
                    IndexNode indexNodeSelected = blockOfIndexToBeRestructured.getIndexData().get(k);
                    if (indexNodeSelected.getLeftBlock() != null || indexNodeSelected.getRightBlock() != null) {
                        counterToInsertBackBlockOfRecord = true;
                        if ((indexNodeSelected.getLeftBlock() != null) && (blockOfRecordsNodesHoldByTheBlockOfIndex.indexOf(indexNodeSelected.getLeftBlock()) < 0)) {
                            blockOfRecordsNodesHoldByTheBlockOfIndex.add(indexNodeSelected.getLeftBlock());
                        }
                        if ((indexNodeSelected.getRightBlock() != null) && (blockOfRecordsNodesHoldByTheBlockOfIndex.indexOf(indexNodeSelected.getRightBlock()) < 0)) {
                            blockOfRecordsNodesHoldByTheBlockOfIndex.add(indexNodeSelected.getRightBlock());
                        }
                    }
                }
                System.out.println("blockOfRecordsNodesHoldByTheBlockOfIndex " + blockOfRecordsNodesHoldByTheBlockOfIndex);


                ArrayList<IndexNode> blockOfIndexToBeRestructuredData = blockOfIndexToBeRestructured.getIndexData();
                IndexNode middleIndexNode = blockOfIndexToBeRestructuredData.get(blockOfIndexToBeRestructured.size() / 2);
                for (int j = 0; j <= blockOfIndexToBeRestructured.size() / 2; j++) {
                    blockIndexSplitPart1.addNode(blockOfIndexToBeRestructuredData.get(j));
                }
                for (int k = (1 + blockOfIndexToBeRestructured.size() / 2); k < blockOfIndexToBeRestructured.size(); k++) {
                    blockIndexSplitPart2.addNode(blockOfIndexToBeRestructuredData.get(k));
                }
                BlockOfIndexNodes upperLevelIndexNode = new BlockOfIndexNodes();
                IndexNode appendedIndexNode = new IndexNode(middleIndexNode.getData(), blockIndexSplitPart1, blockIndexSplitPart2);
                upperLevelIndexNode.addNode(appendedIndexNode);
                blockIndexSplitPart1.setUpperBlock(upperLevelIndexNode);
                blockIndexSplitPart2.setUpperBlock(upperLevelIndexNode);
                index.add(0, blockIndexSplitPart2);
                index.add(0, blockIndexSplitPart1);
                index.add(0, upperLevelIndexNode);
                index.remove(blockOfIndexToBeRestructured);
                String testingString2 = "";
                System.out.println("HEHEHEHEHE" + index.size());
                for (int b = 0; b < index.size(); b++) {
                    for (int a = 0; a < index.get(b).getIndexData().size(); a++) {
                        if ((index.get(b).getIndexData().get(a).getLeft() == null) || (index.get(b).getIndexData().get(a).getRight() == null)) {
                            break;
                        } else {
                            testingString2 += index.get(b).getIndexData().get(a).toStringWithIndexNode() + ", ";
                        }
                    }
                }
                System.out.println("Complete New Index" + testingString2);
                String testingString3 = "";
                for (int b = 0; b < index.size(); b++) {
                    testingString3 += "{";
                    for (int a = 0; a < index.get(b).getIndexData().size(); a++) {
                        testingString3 += index.get(b).getIndexData().get(a).toStringJustIndex() + ", ";
                    }
                    testingString3 += "}";
                }
                System.out.println("Simplified New Index" + testingString3);
                System.out.println(index.get(index.size() - 1).getIndexData().get(0).toStringWithBlock());

//                if (counterToInsertBackBlockOfRecord == true){
//                    for (int c=0; c<blockOfRecordsNodesHoldByTheBlockOfIndex.size();c++){
//                        BlockOfRecordNodes blockOfRecordToBeAdded = blockOfRecordsNodesHoldByTheBlockOfIndex.get(c);
//                        RecordNode veryRightOfBlockOfRecordToBeAdded = blockOfRecordToBeAdded.getBlockData().get(blockOfRecordToBeAdded.size()-1);
//                        String veryRightIndexOfBlockOfRecordToBeAdded = veryRightOfBlockOfRecordToBeAdded.getData().getIndex().toString();
//                        IndexNode startTraversingFromThisIndex = null;
//                        BlockOfIndexNodes highestBlockOfIndex = index.get(0);
//                        int startAtIndexNode = highestBlockOfIndex.size() - 1;
//                        for (int d = 0; d < highestBlockOfIndex.size(); d++) {
//                            String indexOfIndexNodeInHighestBlock = highestBlockOfIndex.getIndexData().get(d).getData().getIndex();
//                            if (indexOfIndexNodeInHighestBlock.compareTo(veryRightIndexOfBlockOfRecordToBeAdded) >= 0) {
//                                startAtIndexNode = d;
//                                break;
//                            }
//                        }
//                        startTraversingFromThisIndex = highestBlockOfIndex.getIndexData().get(startAtIndexNode);
//                        System.out.println(startTraversingFromThisIndex.toStringWithIndexNode());
//                        System.out.println(veryRightIndexOfBlockOfRecordToBeAdded);
//                        System.out.println("gua ngantuk");
//                        BlockOfIndexNodes updatedParentOfBlockOfRecord = traverseTillLastIndexBlock(veryRightIndexOfBlockOfRecordToBeAdded, startTraversingFromThisIndex, highestBlockOfIndex);
//                        System.out.println("updatedParentOfBlockOfRecord Of " + c + " HEy: " + updatedParentOfBlockOfRecord);
//                        for(int e = 0; e<updatedParentOfBlockOfRecord.size();e++){
//                            IndexNode indexChecked = updatedParentOfBlockOfRecord.getIndexData().get(e);
//                            if (updatedParentOfBlockOfRecord.getIndexData().get(e).getData().getIndex().compareTo(veryRightIndexOfBlockOfRecordToBeAdded) == 0){
//                                indexChecked.setLeftPointer(blockOfRecordToBeAdded);
//                                if (updatedParentOfBlockOfRecord.size() > 1){
//                                    updatedParentOfBlockOfRecord.getIndexData().get(e-1).setRightPointer(blockOfRecordToBeAdded);
//                                }
//                                if (updatedParentOfBlockOfRecord.size() > 3){
//                                    updatedParentOfBlockOfRecord.getIndexData().get(e+1).setLeftPointer(blockOfRecordToBeAdded);
//                                }
//                                blockOfRecordToBeAdded.setParentIndexBlock(updatedParentOfBlockOfRecord);
//                                break;
//                            }
//                            else if (updatedParentOfBlockOfRecord.getIndexData().get(e).getData().getIndex().compareTo(veryRightIndexOfBlockOfRecordToBeAdded) > 0){
//                                indexChecked.setRightPointer(blockOfRecordToBeAdded);
//                                if (updatedParentOfBlockOfRecord.size() > 1){
//                                    updatedParentOfBlockOfRecord.getIndexData().get(e-1).setRightPointer(blockOfRecordToBeAdded);
//                                }
//                                if (updatedParentOfBlockOfRecord.size() > 3){
//                                    updatedParentOfBlockOfRecord.getIndexData().get(e+1).setLeftPointer(blockOfRecordToBeAdded);
//                                }
//                                blockOfRecordToBeAdded.setParentIndexBlock(updatedParentOfBlockOfRecord);
//                                break;
//                            }
//                        }
//
//                    }

//                }
            }
        }
    }

    public void addBlock(BlockOfRecordNodes newBlock) {
        this.rootBlocks.add(newBlock);
    }

    public ArrayList<BlockOfRecordNodes> getRootBlocks() {
        return rootBlocks;
    }

    @Override
    public String toString() {
        String stringToBeReturned1 = "ROOT: [";
        for (int a = 0; a < rootBlocks.size(); a++) {
            stringToBeReturned1 += rootBlocks.get(a).toString() + ", ";
        }
        stringToBeReturned1 += "]";
        String stringToBeReturned2 = "INDEX: [";
        for (int a = 0; a < index.size(); a++) {
            stringToBeReturned2 += index.get(a).toString() + ", ";
        }
        stringToBeReturned2 += "]";
        String stringtobereturned = stringToBeReturned1 + "\n" + stringToBeReturned2;
        return stringtobereturned;
    }
}

class BlockOfRecordNodes {
    private ArrayList<RecordNode> blockData = new ArrayList<>();
    private BlockOfRecordNodes neighbourRight = null;
    private BlockOfIndexNodes parentIndexBlock = null;

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

    public BlockOfIndexNodes getParentIndexBlock() {
        return parentIndexBlock;
    }

    public void setParentIndexBlock(BlockOfIndexNodes parentIndexBlock) {
        this.parentIndexBlock = parentIndexBlock;
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

class BlockOfIndexNodes {
    private ArrayList<IndexNode> indexData = new ArrayList<>();
    private BlockOfIndexNodes neighbourRight = null;
    private BlockOfIndexNodes upperBlock = null;

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

    public BlockOfIndexNodes getUpperBlock() {
        return upperBlock;
    }

    public void setNeighbourRight(BlockOfIndexNodes neighbourRight) {
        this.neighbourRight = neighbourRight;
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
    private BlockOfIndexNodes left, right;
    private BlockOfRecordNodes leftBlock, rightBlock;

    public IndexNode() {
        data = new IndexAndDataStored();
        left = null;
        right = null;
    }

    public IndexNode(IndexAndDataStored data, BlockOfIndexNodes left, BlockOfIndexNodes right) {
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

    public void setData(IndexAndDataStored data) {
        this.data = data;
    }

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



