import java.util.ArrayList;

class BPlusTreeIndex {
    private ArrayList<BlockOfRecordNodes> rootBlocks = new ArrayList<>();
    private ArrayList<BlockOfIndexNodes> index = new ArrayList<>();
    private int maximumSize;

    public BPlusTreeIndex(int maximumSize) {
        this.maximumSize = maximumSize;
    }

    // Adding a node into the tree //
    public void addNode(RecordNode rn) {
//        System.out.println(rn.getData().toString());
        if (rootBlocks.size() == 0) {
            BlockOfRecordNodes firstBlock = new BlockOfRecordNodes();
            this.addBlock(firstBlock);
        }
        // Return the block of record nodes that the record node belongs to //
        BlockOfRecordNodes selectedBlock = this.searchWhichBlock(rn);
        // Add the node //
        selectedBlock.addNode(rn);
        // Restructure the tree (restructuring the index is included in the given function) //
        this.restructure();
        while (this.restructureIndex() == true) {
            this.restructureIndex();
        }
    }

    // Search which block does the record node belongs to //
    public BlockOfRecordNodes searchWhichBlock(RecordNode rn) {
        // Get the index of the record node //
        String rnIndex = rn.getData().getIndex();
        // If there is no index yet, return the first block that exist in the tree //
        if (index.isEmpty()) {
            return rootBlocks.get(0);
        } else {
//            System.out.println("Record Node to Search " + rn.getData().getIndex().toString());
//            System.out.println("Block belongs to: " + traverse(rnIndex, index.get(0).getIndexData().get(0)).toString());
            // Used as a parameter for the traverse function. It indicates which IndexNode it should start traversing from //
            IndexNode startTraversingFromThisIndex = null;
            // Get the block of IndexNodes from the highest level of the tree //
            BlockOfIndexNodes highestBlockOfIndex = index.get(0);
            // Serve as a counter. If the BlockOfIndexNodes goes by this [ A, B, C ], we put this counter on the very end (C)
            int startAtIndexNode = highestBlockOfIndex.size() - 1;
            for (int i = 0; i < highestBlockOfIndex.size(); i++) {
                String indexOfIndexNodeInHighestBlock = highestBlockOfIndex.getIndexData().get(i).getData().getIndex();
                // If the index selected (A or B or C) has a greater or equal value than the index that we are searching for,
                // we would start from that particular index of the block
                if (indexOfIndexNodeInHighestBlock.compareTo(rnIndex) >= 0) {
                    startAtIndexNode = i;
                    break;
                }
            }
            // Get the other parameter, which is to indicate which index node from the highest level of tree to start with
            startTraversingFromThisIndex = highestBlockOfIndex.getIndexData().get(startAtIndexNode);
            return traverse(rnIndex, startTraversingFromThisIndex);
        }
    }

    // Similar function as searchWhichBlock, but this returns which IndexBlock it last visits before reaching the root of the tree //
    public BlockOfIndexNodes searchWhichIndexBlockLastVisit(RecordNode rn) {
        String rnIndex = rn.getData().getIndex();
        if (index.isEmpty()) {
            return index.get(0);
        } else {
            // Used as a parameter for the traverse function. It indicates which IndexNode it should start traversing from //
            IndexNode startTraversingFromThisIndex = null;
            // Get the block of IndexNodes from the highest level of the tree //
            BlockOfIndexNodes highestBlockOfIndex = index.get(0);
            // Serve as a counter. If the BlockOfIndexNodes goes by this [ A, B, C ], we put this counter on the very end (C)
            int startAtIndexNode = highestBlockOfIndex.size() - 1;
            for (int i = 0; i < highestBlockOfIndex.size(); i++) {
                String indexOfIndexNodeInHighestBlock = highestBlockOfIndex.getIndexData().get(i).getData().getIndex();
                // If the index selected (A or B or C) has a greater or equal value than the index that we are searching for,
                // we would start from that particular index of the block
                if (indexOfIndexNodeInHighestBlock.compareTo(rnIndex) >= 0) {
                    startAtIndexNode = i;
                    break;
                }
            }
            // Get the other parameter, which is to indicate which index node from the highest level of tree to start with.
            // The highestBlockOfIndex parameter is given to show the last BlockOfIndexNode the function visits.
            startTraversingFromThisIndex = highestBlockOfIndex.getIndexData().get(startAtIndexNode);
            return traverseTillLastIndexBlock(rnIndex, startTraversingFromThisIndex, highestBlockOfIndex);
        }
    }

    // reference https://stackoverflow.com/questions/15306452/traversing-through-all-nodes-of-a-binary-tree-in-java //
    // Used to traverse and search which BlockOfRecordNodes (On the root) that the searched index belongs to
    public BlockOfRecordNodes traverse(String rnIndex, IndexNode root) {
//        System.out.println("RnIndex is " + rnIndex + " root is " + root.getData().toString());
        String rootIndex = root.getData().getIndex();
        // A parameter used to return the result
        BlockOfRecordNodes blockToReturn = null;
//        System.out.println("***************** RECURSION BEGINS ***********************");
        // Used when there's more than 1 level of BlockOfIndexNodes on the tree //
        // If the index to be searched is less or equal to than the value of the selected indexNode, go to its left pointer (which points to a BlockOfIndexNodes) //
//        System.out.println("IF1 " + ((rnIndex.compareTo(rootIndex) <= 0) && (root.getLeft() != null) && (root.getLeftBlock() == null)));
        if ((rnIndex.compareTo(rootIndex) <= 0) && (root.getLeft() != null) && (root.getLeftBlock() == null)) {
//            System.out.println("CHECKPOINT 1");
            BlockOfIndexNodes leftBlockIndexNodes = root.getLeft();
//            System.out.println("You will Go left here  " + leftBlockIndexNodes.toString());
            // Set the index to be 0 if all the if conditions are not passed //
            int indexChosenLocationNumber = 0;
            IndexNode selectedIndexNode = null;
            // Goes through each IndexNode in the BlockOfIndexNode that the pointer selects //
            for (int i = leftBlockIndexNodes.size() - 1; i >= 0; i--) {
                // Checking the index node in the LeftBlockIndexNodes //
                selectedIndexNode = leftBlockIndexNodes.getIndexData().get(i);
//                System.out.println("selectedIndexNode.getData().getIndex() " + selectedIndexNode.getData().getIndex());
//                System.out.println("rootIndex " + rootIndex);
//                System.out.println(selectedIndexNode.getData().getIndex().compareTo(rnIndex));
                // If the index that we are searching for has a bigger index compare to all elements in the LeftBlockIndexNodes //
                // e.g. rnIndex = G, LeftBlockIndexNodes = [A, B, F], since rnIndex is greater than all of them, it should go
                // take the F index and go right of it. [THIS EXAMPLE IS JUST USED FOR A REPRESENTATION]
                if (rnIndex.compareTo(leftBlockIndexNodes.getIndexData().get(leftBlockIndexNodes.size() - 1).getData().getIndex()) > 0) {
                    // It must be on the right of the biggest index element in the LeftBlockIndexNodes, so we take the biggest index element //
                    indexChosenLocationNumber = leftBlockIndexNodes.size() - 1;
                    break;
                }
                // If the element in the LeftBlockIndexNodes is less or equal value compared to the index that we are searching,
                // we will use the element to proceed //
                // e.g. rnIndex = H, LeftBlockIndexNodes = [A, B, C, G, I], we will take element G to proceed [THIS EXAMPLE IS JUST
                // USED FOR A REPRESENTATION] //
                if (i != 0) {
                    IndexNode leftNeighbourOfselectedIndexNode = leftBlockIndexNodes.getIndexData().get(i - 1);
                    if ((selectedIndexNode.getData().getIndex().compareTo(rnIndex) <= 0) || ((leftNeighbourOfselectedIndexNode.getData().getIndex().compareTo(rnIndex) < 0) && (selectedIndexNode.getData().getIndex().compareTo(rnIndex) > 0))) {
//                        System.out.println("REACHED NEW PLACE 1");
                        indexChosenLocationNumber = i - 1;
                        break;
                    }
                }
            }
            // Initialize the node that the index will proceed to search at next //
            selectedIndexNode = leftBlockIndexNodes.getIndexData().get(indexChosenLocationNumber);
            blockToReturn = traverse(rnIndex, selectedIndexNode);
//            if (selectedIndexNode.getLeftBlock() != null) {
//                System.out.println("ASDHAODHODH " + selectedIndexNode.toStringWithBlock());
//            }
        }
        // Used when there's more than 1 level of BlockOfIndexNodes on the tree //
        // If the index to be searched is greater than the value of the selected indexNode, go to its right pointer (which points to a BlockOfIndexNodes) //
//        System.out.println("IF2 " + ((rnIndex.compareTo(rootIndex) > 0) && (root.getRight() != null) && (root.getRightBlock() == null)));
//        System.out.println(root.toStringJustIndex());
//        System.out.println(root.getRight());
//        System.out.println("INDEX" + index.toString());
        if ((rnIndex.compareTo(rootIndex) > 0) && (root.getRight() != null) && (root.getRightBlock() == null)) {
//            System.out.println("CHECKPOINT 2");
            BlockOfIndexNodes rightBlockIndexNodes = root.getRight();
//            System.out.println("You will Go right here  " + rightBlockIndexNodes.toString());
            int indexChosenLocationNumber = 0;
            IndexNode selectedIndexNode = null;
            // Goes through each IndexNode in the BlockOfIndexNode that the pointer selects //
            for (int i = rightBlockIndexNodes.size() - 1; i >= 0; i--) {
                selectedIndexNode = rightBlockIndexNodes.getIndexData().get(i);
//                System.out.println("selectedIndexNode.getData().getIndex() " + selectedIndexNode.getData().getIndex());
//                System.out.println("rootIndex " + rootIndex);
//                System.out.println(selectedIndexNode.getData().getIndex().compareTo(rnIndex));
                // If the next IndexNode to be searched from has a greater value than the previous index //
                if (rnIndex.compareTo(rightBlockIndexNodes.getIndexData().get(rightBlockIndexNodes.size() - 1).getData().getIndex()) > 0) {
//                    System.out.println("reqached here gurll");
                    indexChosenLocationNumber = rightBlockIndexNodes.size() - 1;
                    break;
                }
                if (i != 0) {
                    IndexNode leftNeighbourOfselectedIndexNode = rightBlockIndexNodes.getIndexData().get(i - 1);
                    if ((selectedIndexNode.getData().getIndex().compareTo(rnIndex) <= 0) || ((leftNeighbourOfselectedIndexNode.getData().getIndex().compareTo(rnIndex) < 0) && (selectedIndexNode.getData().getIndex().compareTo(rnIndex) > 0))) {
//                        System.out.println("REACHED NEW PLACE 2");
                        indexChosenLocationNumber = i - 1;
                        break;
                    }
                }
            }
            selectedIndexNode = rightBlockIndexNodes.getIndexData().get(indexChosenLocationNumber);
            blockToReturn = traverse(rnIndex, selectedIndexNode);
        }
        // If it reaches the 1 level above the root, and the index of the IndexNode is lesser or equal to the index to be searched
        // and if there's a BlockOfRecordNode that the IndexNode is pointing to, return the BlockOfRecordNode.
//        System.out.println("IF3 " + ((rnIndex.compareTo(rootIndex) <= 0) && (root.getLeft() == null) && (root.getLeftBlock() != null)));
        if ((rnIndex.compareTo(rootIndex) <= 0) && (root.getLeft() == null) && (root.getLeftBlock() != null)) {
//            System.out.println("Reached return 1");
            return root.getLeftBlock();
        }
        // If it reaches the 1 level above the root, and the index of the IndexNode is lesser or equal to the index to be searched
        // and if there's a BlockOfRecordNode that the IndexNode is pointing to, return the BlockOfRecordNode.
//        System.out.println("IF4 " + ((rnIndex.compareTo(rootIndex) > 0) && (root.getRight() == null) && (root.getRightBlock() != null)));
        if ((rnIndex.compareTo(rootIndex) > 0) && (root.getRight() == null) && (root.getRightBlock() != null)) {
//            System.out.println("Reached return 2");
            return root.getRightBlock();
        }
//        System.out.println("REACHEDE HERE GAIAN");
//        System.out.println("Block to return " + blockToReturn);
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
    // Used to traverse and search which BlockOfRecordNodes (On the root) that the searched index belongs to
    public BlockOfIndexNodes traverseTillLastIndexBlock(String rnIndex, IndexNode root, BlockOfIndexNodes currentlyIn) {
//        System.out.println("currentlyIn " + currentlyIn);
//        System.out.println("RnIndex is " + rnIndex + " root is " + root.getData().toString());
        String rootIndex = root.getData().getIndex();
        // A parameter used to return the result
        BlockOfIndexNodes blockOfIndexesToReturn = null;
//        System.out.println("***************** RECURSION TILL LAST INDEX BLOCK BEGINS ***********************");
        // Used when there's more than 1 level of BlockOfIndexNodes on the tree //
        // If the index to be searched is less or equal to than the value of the selected indexNode, go to its left pointer (which points to a BlockOfIndexNodes) //
//        System.out.println("IF1 " + ((rnIndex.compareTo(rootIndex) <= 0) && (root.getLeft() != null) && (root.getLeftBlock() == null)));
//        System.out.println("PIYE TOH " + root.getLeft());
//        System.out.println(root.getRight());
//        System.out.println((root.getRightBlock() != null));
        if ((rnIndex.compareTo(rootIndex) <= 0) && (root.getLeft() != null) && (root.getLeftBlock() == null)) {
            currentlyIn = root.getLeft();
//            System.out.println("CHECKPOINT 1");
            BlockOfIndexNodes leftBlockIndexNodes = root.getLeft();
            // Set the index to be 0 if all the if conditions are not passed //
            int indexChosenLocationNumber = 0;
            IndexNode selectedIndexNode = null;
            // Goes through each IndexNode in the BlockOfIndexNode that the pointer selects //
            for (int i = leftBlockIndexNodes.size() - 1; i >= 0; i--) {
                // Checking the index node in the LeftBlockIndexNodes //
                selectedIndexNode = leftBlockIndexNodes.getIndexData().get(i);
//                System.out.println("selectedIndexNode.getData().getIndex() " + selectedIndexNode.getData().getIndex());
//                System.out.println("rootIndex " + rootIndex);
//                System.out.println(selectedIndexNode.getData().getIndex().compareTo(rnIndex));
                // If the index that we are searching for has a bigger index compare to all elements in the LeftBlockIndexNodes //
                // e.g. rnIndex = G, LeftBlockIndexNodes = [A, B, F], since rnIndex is greater than all of them, it should go
                // take the F index and go right of it. [THIS EXAMPLE IS JUST USED FOR A REPRESENTATION]
                if (rnIndex.compareTo(leftBlockIndexNodes.getIndexData().get(leftBlockIndexNodes.size() - 1).getData().getIndex()) > 0) {
                    // It must be on the right of the biggest index element in the LeftBlockIndexNodes, so we take the biggest index element //
                    indexChosenLocationNumber = leftBlockIndexNodes.size() - 1;
                    break;
                }
                // If the element in the LeftBlockIndexNodes is less or equal value compared to the index that we are searching,
                // we will use the element to proceed //
                // e.g. rnIndex = H, LeftBlockIndexNodes = [A, B, C, G, I], we will take element G to proceed [THIS EXAMPLE IS JUST
                // USED FOR A REPRESENTATION] //
                if (i != 0) {
                    IndexNode leftNeighbourOfselectedIndexNode = leftBlockIndexNodes.getIndexData().get(i - 1);
                    if ((selectedIndexNode.getData().getIndex().compareTo(rnIndex) <= 0) || ((leftNeighbourOfselectedIndexNode.getData().getIndex().compareTo(rnIndex) < 0) && (selectedIndexNode.getData().getIndex().compareTo(rnIndex) > 0))) {
//                        System.out.println("REACHED NEW PLACE 1 IN TRAVERSE TILL LAST INDEX BLOCK");
                        indexChosenLocationNumber = i - 1;
                        break;
                    }
                }
            }
            // Initialize the node that the index will proceed to search at next //
            selectedIndexNode = leftBlockIndexNodes.getIndexData().get(indexChosenLocationNumber);
//            System.out.println("currentlyIn " + currentlyIn);
//            System.out.println("selectedIndexNode " + selectedIndexNode.toStringJustIndex());
//            if (selectedIndexNode.getLeftBlock() != null) {
//                System.out.println("ncncnncncncnc " + selectedIndexNode.toStringWithBlock());
//            }
            blockOfIndexesToReturn = traverseTillLastIndexBlock(rnIndex, selectedIndexNode, currentlyIn);

        }
        // Used when there's more than 1 level of BlockOfIndexNodes on the tree //
        // If the index to be searched is greater than the value of the selected indexNode, go to its right pointer (which points to a BlockOfIndexNodes) //
//        System.out.println("IF2 " + ((rnIndex.compareTo(rootIndex) > 0) && (root.getRight() != null) && (root.getRightBlock() == null)));
        if ((rnIndex.compareTo(rootIndex) > 0) && (root.getRight() != null) && (root.getRightBlock() == null)) {
            currentlyIn = root.getRight();
//            System.out.println("CHECKPOINT 2");
            BlockOfIndexNodes rightBlockIndexNodes = root.getRight();
            int indexChosenLocationNumber = 0;
            IndexNode selectedIndexNode = null;
            // Goes through each IndexNode in the BlockOfIndexNode that the pointer selects //
            for (int i = rightBlockIndexNodes.size() - 1; i >= 0; i--) {
                selectedIndexNode = rightBlockIndexNodes.getIndexData().get(i);
//                System.out.println("selectedIndexNode.getData().getIndex() " + selectedIndexNode.getData().getIndex());
//                System.out.println("rootIndex " + rootIndex);
//                System.out.println(selectedIndexNode.getData().getIndex().compareTo(rnIndex));
                // If the next IndexNode to be searched from has a greater value than the previous index //
                if (rnIndex.compareTo(rightBlockIndexNodes.getIndexData().get(rightBlockIndexNodes.size() - 1).getData().getIndex()) > 0) {
//                    System.out.println("reqached here gurll");
                    indexChosenLocationNumber = rightBlockIndexNodes.size() - 1;
                    break;
                }
                if (i != 0) {
                    IndexNode leftNeighbourOfselectedIndexNode = rightBlockIndexNodes.getIndexData().get(i - 1);
                    if ((selectedIndexNode.getData().getIndex().compareTo(rnIndex) <= 0) || ((leftNeighbourOfselectedIndexNode.getData().getIndex().compareTo(rnIndex) < 0) && (selectedIndexNode.getData().getIndex().compareTo(rnIndex) > 0))) {
//                        System.out.println("REACHED NEW PLACE 2 IN TRAVERSE TILL LAST INDEX BLOCK");
                        indexChosenLocationNumber = i - 1;
                        break;
                    }
                }
            }
            selectedIndexNode = rightBlockIndexNodes.getIndexData().get(indexChosenLocationNumber);
            blockOfIndexesToReturn = traverseTillLastIndexBlock(rnIndex, selectedIndexNode, currentlyIn);
        }
        // If it reaches the 1 level above the root, and the index of the IndexNode is lesser or equal to the index to be searched
        // and if there's a BlockOfRecordNode that the IndexNode is pointing to, return the BlockOfRecordNode.
//        System.out.println("IF3 " + ((rnIndex.compareTo(rootIndex) <= 0) && (root.getLeft() == null) && (root.getLeftBlock() != null)));
        if ((rnIndex.compareTo(rootIndex) <= 0) && (root.getLeft() == null) && (root.getLeftBlock() != null)) {
//            System.out.println("Reached return 1");
//            System.out.println("currentlyInHIHIHOHO" + currentlyIn);
            blockOfIndexesToReturn = currentlyIn;
            return currentlyIn;
        }
        // If it reaches the 1 level above the root, and the index of the IndexNode is lesser or equal to the index to be searched
        // and if there's a BlockOfRecordNode that the IndexNode is pointing to, return the BlockOfRecordNode.
//        System.out.println("IF4 " + ((rnIndex.compareTo(rootIndex) > 0) && (root.getRight() == null) && (root.getRightBlock() != null)));
        if ((rnIndex.compareTo(rootIndex) > 0) && (root.getRight() == null) && (root.getRightBlock() != null)) {
//            System.out.println("Reached return 2");
            blockOfIndexesToReturn = currentlyIn;
            return currentlyIn;
        }
//        System.out.println("rnIndex " + rnIndex);
//        System.out.println("root.getleft" + root.getLeft());
//        System.out.println((rnIndex.compareTo(rootIndex) <= 0) && (root.getLeft() != null) && (root.getLeftBlock() == null));
//        System.out.println("REACHED HER, againa");
//        System.out.println("blockOfIndexesToReturn " + blockOfIndexesToReturn);
        return blockOfIndexesToReturn;
    }

    public boolean search(String indexToSearch) {

//        System.out.println("//////////////////// LET'S START SEARCHING " + indexToSearch + " /////////////////////");
        boolean foundTheIndex = false;
        // Used as a parameter for the traverse function. It indicates which IndexNode it should start traversing from //
        IndexNode startTraversingFromThisIndex = null;
        // Get the block of IndexNodes from the highest level of the tree //
        BlockOfIndexNodes highestBlockOfIndex = index.get(0);
        // Serve as a counter. If the BlockOfIndexNodes goes by this [ A, B, C ], we put this counter on the very end (C)
        int startAtIndexNode = highestBlockOfIndex.size() - 1;
        for (int i = 0; i < highestBlockOfIndex.size(); i++) {
            String indexOfIndexNodeInHighestBlock = highestBlockOfIndex.getIndexData().get(i).getData().getIndex();
            // If the index selected (A or B or C) has a greater or equal value than the index that we are searching for,
            // we would start from that particular index of the block
            if (indexOfIndexNodeInHighestBlock.compareTo(indexToSearch) >= 0) {
                startAtIndexNode = i;
                break;
            }
        }

        startTraversingFromThisIndex = highestBlockOfIndex.getIndexData().get(startAtIndexNode);
        BlockOfRecordNodes indexToSearchBelongsTo = traverse(indexToSearch, startTraversingFromThisIndex);
//        System.out.println("HMMM " + indexToSearchBelongsTo);
        for (int i = 0; i < indexToSearchBelongsTo.size(); i++) {
//            System.out.println("IndexToSearch" + indexToSearch);
            if (indexToSearchBelongsTo.getBlockData().get(i).getData().getIndex().equals(indexToSearch)) {
                foundTheIndex = true;
                break;
            }
        }
        return foundTheIndex;
    }

    // Restructure the tree //
    public void restructure() {
        for (int i = 0; i < rootBlocks.size(); i++) {
            BlockOfRecordNodes blockSplitPart1 = new BlockOfRecordNodes();
            BlockOfRecordNodes blockSplitPart2 = new BlockOfRecordNodes();
            if (rootBlocks.get(i).size() > maximumSize) {
//                String testingString1 = "Root: [";
//                for (int a = 0; a < rootBlocks.size(); a++) {
//                    testingString1 += rootBlocks.get(a).toString() + ", ";
//                }
//                testingString1 += "]";
//                System.out.println("Initial: " + testingString1);
                BlockOfRecordNodes blockToBeRestructured = rootBlocks.get(i);
//                System.out.println("HEYAFELLA: " + blockToBeRestructured.getParentIndexBlock());
                ArrayList<RecordNode> blockToBeRestructuredData = blockToBeRestructured.getBlockData();
                int indexOfMiddle = -1;
                if (maximumSize == 2) {
                    indexOfMiddle = (blockToBeRestructured.size() / 2);
                }
                if (maximumSize > 2) {
                    indexOfMiddle = (blockToBeRestructured.size() / 2) - 1;
                }
                RecordNode middleNode = blockToBeRestructuredData.get(indexOfMiddle);
                for (int j = 0; j <= indexOfMiddle; j++) {
                    blockSplitPart1.addNode(blockToBeRestructuredData.get(j));
                }
                blockSplitPart1.setNeighbourRight(blockSplitPart2);
                for (int k = (1 + indexOfMiddle); k < blockToBeRestructured.size(); k++) {
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
//                    System.out.println("middleNode" + middleNode.getData().toString());
                } else {
//                    System.out.println("INDEX IS SEO: " + index.toString());
                    BlockOfIndexNodes blockSplitPart1Parent = searchWhichIndexBlockLastVisit(blockSplitPart1.getBlockData().get(blockSplitPart1.size() - 1));
                    blockSplitPart1.setParentIndexBlock(blockSplitPart1Parent);
                    BlockOfIndexNodes blockSplitPart2Parent = searchWhichIndexBlockLastVisit(blockSplitPart2.getBlockData().get(blockSplitPart2.size() - 1));
                    blockSplitPart2.setParentIndexBlock(blockSplitPart2Parent);
                    System.out.println("blockToBeRestructured" + blockToBeRestructured);
                    System.out.println("blockToBeRestructured.getParent " + blockToBeRestructured.getParentIndexBlock());
                    System.out.println("blocksplitpart1Parent " + blockSplitPart2Parent);
                    System.out.println("blocksplitpart2Parent " + blockSplitPart2Parent);
                    IndexNode startTraversingFromThisIndex = null;
                    // STUCK, HOW TO KNOW WHICH BLOCKOFINDEXNODE IN INDEX SHOULD BE PLACED AT //
                    int indexOfParentInIndexVar = index.indexOf(blockToBeRestructured.getParentIndexBlock());
//                    System.out.println("blockToBeRestructured " + blockToBeRestructured);
//                    System.out.println("blockToBeRestructured.getParentIndexBlock()" + blockToBeRestructured.getParentIndexBlock());
//                    System.out.println("indexOfParentInIndexVar " + indexOfParentInIndexVar);
//                    System.out.println("index: " + index.toString());
//                    System.out.println("indexsize " + index.size());
//                    if (index.size() > 1) {
//                        System.out.println(blockToBeRestructured.getParentIndexBlock());
//                        System.out.println("indexget2 " + (index.get(2) == blockToBeRestructured.getParentIndexBlock()));
//                    }
                    System.out.println("index: " + index.toString());
                    System.out.println("root: " + rootBlocks.toString());
                    System.out.println("block to be restructured is " + blockToBeRestructured.toString());
                    System.out.println("block to be restructured parent is " + blockToBeRestructured.getParentIndexBlock().toString());
                    BlockOfIndexNodes indexBlockLocation = index.get(indexOfParentInIndexVar);
                    IndexNode appendedIndexNode = new IndexNode(middleNode.getData(), blockSplitPart1, blockSplitPart2);
//                    System.out.println("PLS WORK");
//
//                    System.out.println(appendedIndexNode.getLeft());
                    // Once the new index has been added, the left and right neighbouring index needs to point at the same updated record of blocks
                    indexBlockLocation.addNode(appendedIndexNode);
//                    System.out.println(index.size());

                    int previousIndexIndex = indexBlockLocation.getIndexData().indexOf(appendedIndexNode) - 1;

                    if ((indexBlockLocation.getIndexData().size() >= 3) && (indexBlockLocation.getIndexData().indexOf(appendedIndexNode) != indexBlockLocation.getIndexData().size() - 1)) {
//                        System.out.println(indexBlockLocation.getIndexData().get(0).toStringWithBlock());
//                        System.out.println(indexBlockLocation.getIndexData().get(1).toStringWithBlock());
//                        System.out.println(indexBlockLocation.getIndexData().get(2).toStringWithBlock());
//                        System.out.println(appendedIndexNode.toStringWithBlock());
                        int rightIndex = indexBlockLocation.getIndexData().indexOf(appendedIndexNode) + 1;
                        indexBlockLocation.getIndexData().get(rightIndex).setLeftPointer(appendedIndexNode.getRightBlock());
                    }
//                    System.out.println(index.size());
                    System.out.println("indexBlockLocation " + indexBlockLocation);
                    System.out.println(previousIndexIndex);
                    if ((indexBlockLocation.getIndexData().size() > 1) && (indexBlockLocation.getIndexData().indexOf(appendedIndexNode) != 0)) {
                        indexBlockLocation.getIndexData().get(previousIndexIndex).setRightPointer(appendedIndexNode.getLeftBlock());
                    }
                    restructureIndex();
                }
//                System.out.println(middleNode.getData().toString());
//                System.out.println("Need restructure");
                rootBlocks.remove(i);
                rootBlocks.add(i, blockSplitPart2);
                rootBlocks.add(i, blockSplitPart1);
//                String testingString = "Root: [";
//                for (int a = 0; a < rootBlocks.size(); a++) {
//                    testingString += rootBlocks.get(a).toString() + ", ";
//                }
//                testingString += "]";
//                System.out.println(testingString);
            }
        }
    }

    public void removeIndexDuplicate() {
        for (int i = 0; i < index.size(); i++) {
            BlockOfIndexNodes indexBeingChecked = index.get(i);
            for (int j = 0; j < index.size(); j++) {
                if ((index.get(j).equals(indexBeingChecked)) && (i != j)) {
                    index.remove(j);
                }
            }
        }
    }

    // Restructure the tree //
    public boolean restructureIndex() {
        boolean returnBool = false;
        for (int i = 0; i < index.size(); i++) {
            BlockOfIndexNodes blockIndexSplitPart1 = new BlockOfIndexNodes();
            BlockOfIndexNodes blockIndexSplitPart2 = new BlockOfIndexNodes();
            if (index.get(i).size() > maximumSize) {
                returnBool = true;
//                String testingString1 = "Index: [";
//                for (int a = 0; a < index.size(); a++) {
//                    testingString1 += index.get(a).toString() + ", ";
//                }
//                testingString1 += "]";
//                System.out.println("Initial: " + testingString1);
                BlockOfIndexNodes blockOfIndexToBeRestructured = index.get(i);
                BlockOfIndexNodes upperBlockOfBlockOfIndexToBeRestructured = blockOfIndexToBeRestructured.getUpperBlock();
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
//                System.out.println("blockOfRecordsNodesHoldByTheBlockOfIndex " + blockOfRecordsNodesHoldByTheBlockOfIndex);


                ArrayList<IndexNode> blockOfIndexToBeRestructuredData = blockOfIndexToBeRestructured.getIndexData();

                int indexOfMiddle = -1;
                if (maximumSize == 2) {
                    indexOfMiddle = (blockOfIndexToBeRestructured.size() / 2);
                }
                if (maximumSize > 2) {
                    indexOfMiddle = (blockOfIndexToBeRestructured.size() / 2) - 1;
                }
                IndexNode middleIndexNode = blockOfIndexToBeRestructuredData.get(indexOfMiddle);
                for (int j = 0; j <= indexOfMiddle; j++) {
                    blockIndexSplitPart1.addNode(blockOfIndexToBeRestructuredData.get(j));
                }
                for (int k = (1 + indexOfMiddle); k < blockOfIndexToBeRestructured.size(); k++) {
                    blockIndexSplitPart2.addNode(blockOfIndexToBeRestructuredData.get(k));
                }
                BlockOfIndexNodes upperLevelIndexNode = new BlockOfIndexNodes();
                IndexNode appendedIndexNode = new IndexNode(middleIndexNode.getData(), blockIndexSplitPart1, blockIndexSplitPart2);
                upperLevelIndexNode.addNode(appendedIndexNode);
                blockIndexSplitPart1.setUpperBlock(upperLevelIndexNode);
                blockIndexSplitPart2.setUpperBlock(upperLevelIndexNode);
                if (upperBlockOfBlockOfIndexToBeRestructured == null) {
                    for (int b = 0; b < upperLevelIndexNode.size() - 1; b++) {
//                        System.out.println("WWWWWW" + upperLevelIndexNode.getIndexData().get(b).getData().toString());
                        BlockOfRecordNodes makeItNull = null;
                        upperLevelIndexNode.getIndexData().get(b).setLeftPointer(makeItNull);
                        upperLevelIndexNode.getIndexData().get(b).setRightPointer(makeItNull);
                    }
                    index.add(0, blockIndexSplitPart2);
                    index.add(0, blockIndexSplitPart1);
//                    System.out.println("DOGGO " + upperLevelIndexNode.getIndexData().get(0).getData());
//                    System.out.println("PUGGY " + upperLevelIndexNode.getIndexData().get(0).getLeftBlock());
//                    System.out.println("PUGGY " + upperLevelIndexNode.getIndexData().get(0).getLeft());
                    index.add(0, upperLevelIndexNode);
//                    System.out.println("UPPERLEVEL" + upperLevelIndexNode.toString());

                    index.remove(blockOfIndexToBeRestructured);
//                    System.out.println("Here lies the problem " + index.toString());
//                    System.out.println("last index " + index.get(2));
//                    String testingString2 = "";
//                    System.out.println("HEHEHEHEHE" + index.size());
//                    for (int b = 0; b < index.size(); b++) {
//                        for (int a = 0; a < index.get(b).getIndexData().size(); a++) {
//                            if ((index.get(b).getIndexData().get(a).getLeft() == null) || (index.get(b).getIndexData().get(a).getRight() == null)) {
//                                break;
//                            } else {
//                                testingString2 += index.get(b).getIndexData().get(a).toStringWithIndexNode() + ", ";
//                            }
//                        }
//                    }
//                    System.out.println("Complete New Index" + testingString2);
//                    String testingString3 = "";
//                    for (int b = 0; b < index.size(); b++) {
//                        testingString3 += "{";
//                        for (int a = 0; a < index.get(b).getIndexData().size(); a++) {
//                            testingString3 += index.get(b).getIndexData().get(a).toStringJustIndex() + "|||||| ";
//                        }
//                        testingString3 += "}";
//                    }
//                    System.out.println("Simplified New Index" + testingString3);
//                    System.out.println(index.get(index.size() - 1).getIndexData().get(0).toStringWithBlock());
                } else {
//                    System.out.println("NANA " + blockOfIndexToBeRestructured);
//                    System.out.println("NANANA " + upperBlockOfBlockOfIndexToBeRestructured);
                    upperBlockOfBlockOfIndexToBeRestructured.addNode(appendedIndexNode);
                    // Index (int) in the new block of the newly added IndexNode
                    int indexOfAppendedIndexNode = upperBlockOfBlockOfIndexToBeRestructured.getIndexData().indexOf(appendedIndexNode);
                    // Set the left neighbour with the newest value to its right pointer
                    System.out.println("upperBlockOfIndexTobeRestructured: " + upperBlockOfBlockOfIndexToBeRestructured.toString());
                    System.out.println("upperBlockOfIndexTobeRestructuredParents: " + upperBlockOfBlockOfIndexToBeRestructured.getUpperBlock());
                    System.out.println("appendedIndexNode " + appendedIndexNode.toStringJustIndex());
                    if (indexOfAppendedIndexNode - 1 > 0) {
                        System.out.println("HEHE" + upperBlockOfBlockOfIndexToBeRestructured.getIndexData().get(indexOfAppendedIndexNode - 1).toStringJustIndex());
                        upperBlockOfBlockOfIndexToBeRestructured.getIndexData().get(indexOfAppendedIndexNode - 1).setRightPointer(blockIndexSplitPart1);
                    }
                    // Set the right neighbour with the newest value to its left pointer
                    if (indexOfAppendedIndexNode + 1 < upperBlockOfBlockOfIndexToBeRestructured.size()) {
                        System.out.println("HUHU " + upperBlockOfBlockOfIndexToBeRestructured.getIndexData().get(indexOfAppendedIndexNode + 1).toStringJustIndex());
                        upperBlockOfBlockOfIndexToBeRestructured.getIndexData().get(indexOfAppendedIndexNode + 1).setLeftPointer(blockIndexSplitPart2);
                    }
                    blockIndexSplitPart1.setUpperBlock(upperBlockOfBlockOfIndexToBeRestructured);
                    blockIndexSplitPart2.setUpperBlock(upperBlockOfBlockOfIndexToBeRestructured);
                    for (int b = 0; b < upperBlockOfBlockOfIndexToBeRestructured.size() - 1; b++) {
                        BlockOfRecordNodes makeItNull = null;
                        upperBlockOfBlockOfIndexToBeRestructured.getIndexData().get(b).setLeftPointer(makeItNull);
                        upperBlockOfBlockOfIndexToBeRestructured.getIndexData().get(b).setRightPointer(makeItNull);
                    }
//                    System.out.println("DDD INDEX: " + index);
                    index.add(0, blockIndexSplitPart2);
                    index.add(0, blockIndexSplitPart1);
                    index.add(0, upperBlockOfBlockOfIndexToBeRestructured);
                    index.remove(blockOfIndexToBeRestructured);
//                    System.out.println("DDD1 INDEX: " + index);
//                    System.out.println("Here lies the problem " + index.toString());
//                    System.out.println("last index " + index.get(2));
//                    String testingString2 = "";
//                    if (index.size() == 5) {
//                        System.out.println("DIGGITY " + index.get(3).getIndexData().get(1).toStringWithIndexNode());
//                    }
//
//                    System.out.println("HEHEHEHEHE" + index.size());
//                    for (int b = 0; b < index.size(); b++) {
//                        for (int a = 0; a < index.get(b).getIndexData().size(); a++) {
//                            if ((index.get(b).getIndexData().get(a).getLeft() == null) || (index.get(b).getIndexData().get(a).getRight() == null)) {
//                                break;
//                            } else {
//                                testingString2 += index.get(b).getIndexData().get(a).toStringWithIndexNode() + ", ";
//                            }
//                        }
//                    }
//                    System.out.println("Complete New Index" + testingString2);
//                    String testingString3 = "";
//                    for (int b = 0; b < index.size(); b++) {
//                        testingString3 += "{";
//                        for (int a = 0; a < index.get(b).getIndexData().size(); a++) {
//                            testingString3 += index.get(b).getIndexData().get(a).toStringJustIndex() + "|||||| ";
//                        }
//                        testingString3 += "}";
//                    }
//                    System.out.println("Simplified New Index" + testingString3);
//                    System.out.println(index.get(index.size() - 1).getIndexData().get(0).toStringWithBlock());
                }

//                System.out.println("MAKNAE " + counterToInsertBackBlockOfRecord);
                if (counterToInsertBackBlockOfRecord == true) {
                    System.out.println("HAHA" + blockIndexSplitPart1.toString());
                    System.out.println("HOHO " + blockIndexSplitPart2.toString());
                    for (int c = 0; c < blockIndexSplitPart1.size() - 1; c++) {
                        BlockOfRecordNodes makeItNull = new BlockOfRecordNodes();
                        blockIndexSplitPart1.getIndexData().get(c).setLeftPointer(makeItNull);
                        blockIndexSplitPart1.getIndexData().get(c).setRightPointer(makeItNull);
                    }
                    for (int c = 0; c < blockIndexSplitPart2.size() - 1; c++) {
                        BlockOfRecordNodes makeItNull = new BlockOfRecordNodes();
                        blockIndexSplitPart2.getIndexData().get(c).setLeftPointer(makeItNull);
                        blockIndexSplitPart2.getIndexData().get(c).setRightPointer(makeItNull);
                    }
                    for (int c = 0; c < blockOfRecordsNodesHoldByTheBlockOfIndex.size(); c++) {
                        BlockOfRecordNodes blockOfRecordToBeAdded = blockOfRecordsNodesHoldByTheBlockOfIndex.get(c);
                        RecordNode veryRightOfBlockOfRecordToBeAdded = blockOfRecordToBeAdded.getBlockData().get(blockOfRecordToBeAdded.size() - 1);
                        String veryRightIndexOfBlockOfRecordToBeAdded = veryRightOfBlockOfRecordToBeAdded.getData().getIndex().toString();
                        IndexNode startTraversingFromThisIndex = null;
                        BlockOfIndexNodes highestBlockOfIndex = index.get(0);
                        int startAtIndexNode = highestBlockOfIndex.size() - 1;
                        for (int d = 0; d < highestBlockOfIndex.size(); d++) {
                            String indexOfIndexNodeInHighestBlock = highestBlockOfIndex.getIndexData().get(d).getData().getIndex();
                            if (indexOfIndexNodeInHighestBlock.compareTo(veryRightIndexOfBlockOfRecordToBeAdded) >= 0) {
                                startAtIndexNode = d;
                                break;
                            }
                        }
                        startTraversingFromThisIndex = highestBlockOfIndex.getIndexData().get(startAtIndexNode);
//                        System.out.println(startTraversingFromThisIndex.toStringWithIndexNode());
//                        System.out.println(veryRightIndexOfBlockOfRecordToBeAdded);
//                        System.out.println("gua ngantuk");
//                        System.out.println("bobo dong " + index.get(0).getIndexData().get(0).getLeftBlock());
                        BlockOfIndexNodes updatedParentOfBlockOfRecord = traverseTillLastIndexBlock(veryRightIndexOfBlockOfRecordToBeAdded, startTraversingFromThisIndex, highestBlockOfIndex);
                        System.out.println("THIS IS TO BE ADDED : " + blockOfRecordToBeAdded + " updateedBlockOfParentIs: " + updatedParentOfBlockOfRecord);
                        for (int e = 0; e < updatedParentOfBlockOfRecord.size(); e++) {
                            IndexNode indexChecked = updatedParentOfBlockOfRecord.getIndexData().get(e);
//                            System.out.println("blockOfRecordToBeAdded " + blockOfRecordToBeAdded.toString());
//                            System.out.println("WHAT? " + indexChecked.toStringJustIndex());
//                            System.out.println("ALL THE INDEX: " + index.toString());
//                            System.out.println("blockOfRecordToBeAdded.getParentIndexBlock() " + blockOfRecordToBeAdded.getParentIndexBlock());
//                            System.out.println("updatedParentOfBlockOfRecord " + updatedParentOfBlockOfRecord);
                            blockOfRecordToBeAdded.setParentIndexBlock(updatedParentOfBlockOfRecord);
                            if (updatedParentOfBlockOfRecord.getIndexData().get(e).getData().getIndex().compareTo(veryRightIndexOfBlockOfRecordToBeAdded) >= 0) {

                                if (updatedParentOfBlockOfRecord.getUpperBlock() != null) {
                                    indexChecked.setLeftPointer(blockOfRecordToBeAdded);
                                }

                                if ((updatedParentOfBlockOfRecord.size() >= 3) && (e > 0)) {
                                    updatedParentOfBlockOfRecord.getIndexData().get(e - 1).setRightPointer(blockOfRecordToBeAdded);

                                }
                                if ((updatedParentOfBlockOfRecord.size() > 3) && (e < updatedParentOfBlockOfRecord.size() - 1)) {

                                    updatedParentOfBlockOfRecord.getIndexData().get(e + 1).setLeftPointer(blockOfRecordToBeAdded);
                                }

//                                System.out.println("DONE1");
                                break;
                            } else if (updatedParentOfBlockOfRecord.getIndexData().get(e).getData().getIndex().compareTo(veryRightIndexOfBlockOfRecordToBeAdded) < 0) {
                                if (updatedParentOfBlockOfRecord.getUpperBlock() != null) {
                                    indexChecked.setRightPointer(blockOfRecordToBeAdded);
                                }
//                                System.out.println("YOU MUST BE HERE");
                                if ((updatedParentOfBlockOfRecord.size() >= 3) && (e > 0)) {
                                    updatedParentOfBlockOfRecord.getIndexData().get(e - 1).setRightPointer(blockOfRecordToBeAdded);
//                                    System.out.println("HERE IS A DOLLAR " + updatedParentOfBlockOfRecord.getIndexData().get(e).toStringJustIndex());
//                                    System.out.println("SHY LITTLE FROG " + indexChecked.getRightBlock());
                                }
                                if ((updatedParentOfBlockOfRecord.size() > 3) && (e < updatedParentOfBlockOfRecord.size() - 1)) {
                                    updatedParentOfBlockOfRecord.getIndexData().get(e + 1).setLeftPointer(blockOfRecordToBeAdded);
                                }
//                                System.out.println("DONE2");
                                break;
                            }
                        }

                    }
//                    System.out.println("NEW INDEX" + index.toString());
                }
                removeIndexDuplicate();
                returnBool = true;
            }
        }
        return returnBool;
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


// BACKUP FOR TRAVERSE traverseTillLastIndexBlock

//    // reference https://stackoverflow.com/questions/15306452/traversing-through-all-nodes-of-a-binary-tree-in-java //
//    // Similar function as before, but used to traverse and search the last IndexBlock it visits //
//    public BlockOfIndexNodes traverseTillLastIndexBlock(String rnIndex, IndexNode root, BlockOfIndexNodes currentlyIn) {
//        String rootIndex = root.getData().getIndex();
//        // A parameter used to return the result
//        BlockOfIndexNodes blockOfIndexNodesToReturn = null;
//        // Used to get the other blockOfIndexNodes that the IndexNode is pointing to
//        BlockOfIndexNodes leftBlockIndexNodes = root.getLeft();
//        BlockOfIndexNodes rightBlockIndexNodes = root.getRight();
//        // Used when there's more than 1 level of BlockOfIndexNodes on the tree //
//        // If the index to be searched is less or equal to than the value of the selected indexNode, go to its left pointer (which points to a BlockOfIndexNodes) //
//        if ((rnIndex.compareTo(rootIndex) <= 0) && (root.getLeft() != null) && (root.getLeftBlock() == null)) {
//            for (int i = 0; i < leftBlockIndexNodes.size(); i++) {
//                IndexNode selectedIndexNode = leftBlockIndexNodes.getIndexData().get(i);
//                if (i != leftBlockIndexNodes.size() - 1) {
//                    IndexNode nextSelectedIndexNode = leftBlockIndexNodes.getIndexData().get(i + 1);
//                    if ((selectedIndexNode.getData().getIndex().compareTo(rootIndex) <= 0) && (nextSelectedIndexNode.getData().getIndex().compareTo(rootIndex) >= 1)) {
//                        blockOfIndexNodesToReturn = traverseTillLastIndexBlock(rnIndex, selectedIndexNode, leftBlockIndexNodes);
//                    }
//                } else {
//                    blockOfIndexNodesToReturn = traverseTillLastIndexBlock(rnIndex, selectedIndexNode, leftBlockIndexNodes);
//                }
//
//            }
//        }
//        if ((rnIndex.compareTo(rootIndex) > 0) && (root.getRight() != null) && (root.getRightBlock() == null)) {
//            for (int i = 0; i < rightBlockIndexNodes.size(); i++) {
//                IndexNode selectedIndexNode = rightBlockIndexNodes.getIndexData().get(i);
//                if (i != rightBlockIndexNodes.size() - 1) {
//                    IndexNode nextSelectedIndexNode = rightBlockIndexNodes.getIndexData().get(i + 1);
//                    if ((selectedIndexNode.getData().getIndex().compareTo(rootIndex) <= 0) && (nextSelectedIndexNode.getData().getIndex().compareTo(rootIndex) >= 1)) {
//                        blockOfIndexNodesToReturn = traverseTillLastIndexBlock(rnIndex, selectedIndexNode, leftBlockIndexNodes);
//                    }
//                } else {
//                    blockOfIndexNodesToReturn = traverseTillLastIndexBlock(rnIndex, selectedIndexNode, rightBlockIndexNodes);
//                }
//            }
//        }
//        if ((rnIndex.compareTo(rootIndex) <= 0) && (root.getLeft() == null) && (root.getLeftBlock() != null)) {
//            System.out.println(currentlyIn.toString());
//            System.out.println("Reached 3");
//            return currentlyIn;
//        }
//        if ((rnIndex.compareTo(rootIndex) > 0) && (root.getRight() == null) && (root.getRightBlock() != null)) {
//            System.out.println("REached 4");
//            return currentlyIn;
//        }
//        return blockOfIndexNodesToReturn;
//    }