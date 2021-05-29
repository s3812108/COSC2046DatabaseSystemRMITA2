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
//        System.out.println("****************************** RECORD NODE TO BE ADDED " + rn.toString());
        if (rootBlocks.size() == 0) {
            BlockOfRecordNodes firstBlock = new BlockOfRecordNodes();
            this.addBlock(firstBlock);
        }
        // Return the block of record nodes that the record node belongs to //
        BlockOfRecordNodes selectedBlock = this.searchWhichBlock(rn);
//        System.out.println("****************************** SELECTEDBLOCK " + selectedBlock.toString());
        // Add the node //
        selectedBlock.addNode(rn);
        // Restructure the tree (restructuring the index is included in the given function) //
        this.restructure();
        this.restructureIndex();
//        System.out.println("IT'S TIME TO ADD " + rn.getData().getIndex());
//        if (index.size() > 3) {
//            String test = "[";
//            for (int i = 0; i < rootBlocks.size(); i++) {
//                test += rootBlocks.get(i).getParentIndexBlock().size();
//                test += ", ";
//                if (rootBlocks.get(i).getParentIndexBlock().size() > maximumSize){
//                    System.out.println("THIS IS THE PARENT PROBLEM: " + rootBlocks.get(i).getParentIndexBlock().toString());
//                    System.out.println("THIS IS THE VICTIM RECORD BLOCK: " + rootBlocks.get(i).toString());
//                    System.out.println("index.get(0) " + index.get(0));
//                    BlockOfIndexNodes test999 = searchWhichIndexBlockLastVisit(rootBlocks.get(i).getBlockData().get(rootBlocks.get(i).size()-1));
//                    System.out.println("HOYYYYY " + test999);
//                    System.out.println("THE TOP: " + test999.getUpperBlock());
//                    for (int zz = 0; zz < index.size(); zz++){
//                        for (int j = 0; j < index.get(zz).size(); j++){
//                            if (index.get(zz).getIndexData().get(j).getData().getIndex().equals("211/01/2019 06:00:00 PM")){
//                                System.out.println("HOPEFULLY: " + index.get(zz).getIndexData().get(j).getLeft());
//                                break;
//                            }
//                        }
//                    }
//                    System.exit(0);
//                }
//            }
//            test += "]";
//            System.out.println("PARENT SIZE: " + test);
//
//        }
//        String test1 = "[";
//        for (int i = 0; i < index.size(); i++) {
//            test1 += index.get(i).getIndexData().size();
//            test1 += ", ";
//        }
//        test1 += "]";
//        System.out.println("INDEX SIZE: " + test1);
//        System.out.println("Root size: " + rootBlocks.size());
//        System.out.println("");
//        System.out.println("INDEX: " + index.toString());
//        System.out.println("ROOT: " + rootBlocks.toString());
//        System.out.println("-------------------------------------------------------------------------------------------");
//        for (int i = 0; i < index.size(); i++) {
//            for (int j = 0; j < index.get(i).size(); j++) {
//                if (((index.get(i).getIndexData().get(j).getLeft() != null) || (index.get(i).getIndexData().get(j).getRight() != null)) && ((index.get(i).getIndexData().get(j).getLeftBlock() == null) || (index.get(i).getIndexData().get(j).getRightBlock() == null))) {
//                    String test123 = "Index with index: " + index.get(i).getIndexData().get(j).toStringJustIndex();
//                    if (index.get(i).getIndexData().get(j).getLeft() == null) {
//                        test123 += " left: []";
//                    } else {
//                        test123 += " left: " + index.get(i).getIndexData().get(j).getLeft().toString();
//                    }
//                    if (index.get(i).getIndexData().get(j).getRight() == null) {
//                        test123 += " right: []";
//                    } else {
//                        test123 += " right: " + index.get(i).getIndexData().get(j).getRight().toString();
//                    }
//                    System.out.println(test123);
//                }
//            }
//            for (int j = 0; j < index.get(i).size(); j++) {
//                if ((index.get(i).getIndexData().get(j).getLeftBlock() != null) || (index.get(i).getIndexData().get(j).getRightBlock() != null)) {
//                    System.out.println("Index with block: ````" + index.get(i).getIndexData().size() + "```` " + index.get(i).getIndexData().get(j).toStringJustIndex() + index.get(i).getIndexData().get(j).getLeftBlock().toString() + " Rightblock: " + index.get(i).getIndexData().get(j).getRightBlock().toString());
//                }
//            }
//        }
    }

    // Search which block does the record node belongs to //
    public BlockOfRecordNodes searchWhichBlock(RecordNode rn) {
        // Get the index of the record node //
        String rnIndex = rn.getData().getIndex();
        // If there is no index yet, return the first block that exist in the tree //
        if (index.isEmpty()) {
            return rootBlocks.get(0);
        } else {
            // Used as a parameter for the traverse function. It indicates which IndexNode it should start traversing from //
            IndexNode startTraversingFromThisIndex = null;
            // Get the block of IndexNodes from the highest level of the tree //
            BlockOfIndexNodes highestBlockOfIndexTree = index.get(0);
            // Serve as a counter. If the BlockOfIndexNodes goes by this [ A, B, C ], we put this counter on the very end (C)
            int startAtIndexNode = highestBlockOfIndexTree.size() - 1;
            for (int i = 0; i < highestBlockOfIndexTree.size(); i++) {
                String eachIndexInHighestBlock = highestBlockOfIndexTree.getIndexData().get(i).getData().getIndex();
                if (i > 0) {
                    // Going through the indexes in the highest block of the tree //
                    String leftNeighbourOfEachIndexInHighestBlock = highestBlockOfIndexTree.getIndexData().get(i - 1).getData().getIndex();
                    /* If the index (being checked) on the highest block of tree is greater and the left neighbour index of index (being checked) has a smaller value
                     * compared to the search index, return the left neighbour index of index (being checked)
                     * e.g. Block: [ A D ] & rn: B, D > B && A < B, then take the A index.
                     */
                    if (eachIndexInHighestBlock.compareTo(rnIndex) >= 0) {
                        startAtIndexNode = i;
                        break;
                    }
                    if ((eachIndexInHighestBlock.compareTo(rnIndex) < 0) && (leftNeighbourOfEachIndexInHighestBlock.compareTo(rnIndex) > 0)) {
                        startAtIndexNode = i - 1;
                        break;
                    }
                }
                if (eachIndexInHighestBlock.compareTo(rnIndex) >= 0) {
                    startAtIndexNode = i;
                    break;
                }
            }
            // Get the other parameter, which is to indicate which index node from the highest level of tree to start with
            startTraversingFromThisIndex = highestBlockOfIndexTree.getIndexData().get(startAtIndexNode);
//            System.out.println("STARTTRAVERSINGFROMTHISINDEX: " + startTraversingFromThisIndex.toStringJustIndex());
            // Traverse until the allocated block record on the root of the tree is returned //
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
            BlockOfIndexNodes highestBlockOfIndexTree = index.get(0);
            // Serve as a counter. If the BlockOfIndexNodes goes by this [ A, B, C ], we put this counter on the very end (C)
            int startAtIndexNode = highestBlockOfIndexTree.size() - 1;
            for (int i = 0; i < highestBlockOfIndexTree.size(); i++) {
                String eachIndexInHighestBlock = highestBlockOfIndexTree.getIndexData().get(i).getData().getIndex();
                if (i > 0) {
                    // Going through the indexes in the highest block of the tree //
                    String leftNeighbourOfEachIndexInHighestBlock = highestBlockOfIndexTree.getIndexData().get(i - 1).getData().getIndex();
                    /* If the index (being checked) on the highest block of tree is greater and the left neighbour index of index (being checked) has a smaller value
                     * compared to the search index, return the left neighbour index of index (being checked)
                     * e.g. Block: [ A D ] & rn: B, D > B && A < B, then take the A index.
                     */
                    if (eachIndexInHighestBlock.compareTo(rnIndex) >= 0) {
                        startAtIndexNode = i;
                        break;
                    }
                    if ((eachIndexInHighestBlock.compareTo(rnIndex) < 0) && (leftNeighbourOfEachIndexInHighestBlock.compareTo(rnIndex) > 0)) {
                        startAtIndexNode = i - 1;
                        break;
                    }
                }
                if (eachIndexInHighestBlock.compareTo(rnIndex) >= 0) {
                    startAtIndexNode = i;
                    break;
                }
            }
            // Get the other parameter, which is to indicate which index node from the highest level of tree to start with.
            // The highestBlockOfIndex parameter is given to show the last BlockOfIndexNode the function visits.
            startTraversingFromThisIndex = highestBlockOfIndexTree.getIndexData().get(startAtIndexNode);
            // Traverse until the last visited block of indexes is returned //
//            System.out.println("highestBlockOfIndexTree: " + highestBlockOfIndexTree);
            return traverseTillLastIndexBlock(rnIndex, startTraversingFromThisIndex, highestBlockOfIndexTree);
        }
    }

    // codeMan. (2013). Traversing through all nodes of a binary tree in Java. Stackoverflow. https://stackoverflow.com/questions/15306452/traversing-through-all-nodes-of-a-binary-tree-in-java. //
    // Used to traverse and search which BlockOfRecordNodes (On the root) that the searched index belongs to
    public BlockOfRecordNodes traverse(String rnIndex, IndexNode root) {
//        System.out.println("index " + index);
//        System.out.println("RnIndex is " + rnIndex + " root is " + root.getData().toString());
        String rootIndex = root.getData().getIndex();
        // A parameter used to return the result
        BlockOfRecordNodes blockToReturn = null;
//        System.out.println("***************** RECURSION BEGINS ***********************");
        // Used when there's more than 1 level of BlockOfIndexNodes on the tree //
        // If the index to be searched is less or equal to than the value of the selected indexNode, go to its left pointer (which points to a BlockOfIndexNodes) //
        if ((rnIndex.compareTo(rootIndex) <= 0) && (root.getLeft() != null) && (root.getLeft().size() != 0) && (root.getLeftBlock() == null)) {
//            System.out.println("CHECKPOINT 1");
            BlockOfIndexNodes leftBlockIndexNodes = root.getLeft();
            // Set the index to be 0 if all the if conditions are not passed //
            int indexChosenLocationNumber = 0;
            IndexNode selectedIndexNode = null;
            // Goes through each IndexNode in the BlockOfIndexNode that the pointer selects //
            for (int i = leftBlockIndexNodes.size() - 1; i >= 0; i--) {
                // Checking the index node in the LeftBlockIndexNodes //
                selectedIndexNode = leftBlockIndexNodes.getIndexData().get(i);
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
                    if ((selectedIndexNode.getData().getIndex().compareTo(rnIndex) <= 0)) {
                        indexChosenLocationNumber = i;
                        break;
                    }
                    if ((leftNeighbourOfselectedIndexNode.getData().getIndex().compareTo(rnIndex) < 0) && (selectedIndexNode.getData().getIndex().compareTo(rnIndex) > 0)) {
                        indexChosenLocationNumber = i - 1;
                        break;
                    }
                }
            }
            // Initialize the node that the index will proceed to search at next //
            selectedIndexNode = leftBlockIndexNodes.getIndexData().get(indexChosenLocationNumber);
            blockToReturn = traverse(rnIndex, selectedIndexNode);
        }
        // Used when there's more than 1 level of BlockOfIndexNodes on the tree //
        // If the index to be searched is greater than the value of the selected indexNode, go to its right pointer (which points to a BlockOfIndexNodes) //
        if ((rnIndex.compareTo(rootIndex) > 0) && (root.getRight() != null) && (root.getRight().size() != 0) && (root.getRightBlock() == null)) {
//            System.out.println("CHECKPOINT 2");
            BlockOfIndexNodes rightBlockIndexNodes = root.getRight();
            int indexChosenLocationNumber = 0;
            IndexNode selectedIndexNode = null;
            // Goes through each IndexNode in the BlockOfIndexNode that the pointer selects //
            for (int i = rightBlockIndexNodes.size() - 1; i >= 0; i--) {
                // Checking the index node in the RightBlockIndexNodes //
                selectedIndexNode = rightBlockIndexNodes.getIndexData().get(i);
                // If the index that we are searching for has a bigger index compare to all elements in the RightBlockIndexNodes //
                // e.g. rnIndex = G, RightBlockIndexNodes = [A, B, F], since rnIndex is greater than all of them, it should go
                // take the F index and go right of it. [THIS EXAMPLE IS JUST USED FOR A REPRESENTATION]
                if (rnIndex.compareTo(rightBlockIndexNodes.getIndexData().get(rightBlockIndexNodes.size() - 1).getData().getIndex()) > 0) {
                    indexChosenLocationNumber = rightBlockIndexNodes.size() - 1;
                    break;
                }
                // If the element in the RightBlockIndexNodes is less or equal value compared to the index that we are searching,
                // we will use the element to proceed //
                // e.g. rnIndex = H, RightBlockIndexNodes = [A, B, C, G, I], we will take element G to proceed [THIS EXAMPLE IS JUST
                // USED FOR A REPRESENTATION] //
                if (i != 0) {
                    IndexNode leftNeighbourOfselectedIndexNode = rightBlockIndexNodes.getIndexData().get(i - 1);
//                    System.out.println("NYAMPE SINI GAK");
                    if ((selectedIndexNode.getData().getIndex().compareTo(rnIndex) <= 0)) {
//                        System.out.println("nyampe sini gak1");
                        indexChosenLocationNumber = i;
                        break;
                    }
                    if ((leftNeighbourOfselectedIndexNode.getData().getIndex().compareTo(rnIndex) < 0) && (selectedIndexNode.getData().getIndex().compareTo(rnIndex) > 0)) {
                        indexChosenLocationNumber = i - 1;
                        break;
                    }
                }
            }
            // Initialize the node that the index will proceed to search at next //
            selectedIndexNode = rightBlockIndexNodes.getIndexData().get(indexChosenLocationNumber);
            blockToReturn = traverse(rnIndex, selectedIndexNode);
        }
        // If it reaches the 1 level above the root, and the index of the IndexNode is lesser or equal to the index to be searched
        // and if there's a BlockOfRecordNode that the IndexNode is pointing to, return the BlockOfRecordNode.
        if ((rnIndex.compareTo(rootIndex) <= 0) && (root.getLeft() == null) && (root.getLeftBlock() != null)) {
//            System.out.println("Reached return 1");
            return root.getLeftBlock();
        }
        // If it reaches the 1 level above the root, and the index of the IndexNode is lesser or equal to the index to be searched
        // and if there's a BlockOfRecordNode that the IndexNode is pointing to, return the BlockOfRecordNode.
        if ((rnIndex.compareTo(rootIndex) > 0) && (root.getRight() == null) && (root.getRightBlock() != null)) {
//            System.out.println("Reached return 2");
            return root.getRightBlock();
        }
        return blockToReturn;
    }


    // codeMan. (2013). Traversing through all nodes of a binary tree in Java. Stackoverflow. https://stackoverflow.com/questions/15306452/traversing-through-all-nodes-of-a-binary-tree-in-java. //
    // Used to traverse and search which BlockOfRecordNodes (On the root) that the searched index belongs to
    public BlockOfIndexNodes traverseTillLastIndexBlock(String rnIndex, IndexNode root, BlockOfIndexNodes currentlyIn) {
        String rootIndex = root.getData().getIndex();
        // A parameter used to return the result
        BlockOfIndexNodes blockOfIndexesToReturn = null;
//        System.out.println("currentlyIn " + currentlyIn.toString());
        // Used when there's more than 1 level of BlockOfIndexNodes on the tree //
        // If the index to be searched is less or equal to than the value of the selected indexNode, go to its left pointer (which points to a BlockOfIndexNodes) //
        if ((rnIndex.compareTo(rootIndex) <= 0) && (root.getLeft() != null) && (root.getLeftBlock() == null)) {
            // Set the block of index nodes that it is currently in //
            currentlyIn = root.getLeft();
            BlockOfIndexNodes leftBlockIndexNodes = root.getLeft();
            // Set the index to be 0 if all the if conditions are not passed //
            int indexChosenLocationNumber = 0;
            // The next IndexNode that the function would traverse to //
            IndexNode selectedIndexNode = null;
            // Goes through each IndexNode in the BlockOfIndexNode that the pointer selects //
            for (int i = leftBlockIndexNodes.size() - 1; i >= 0; i--) {
                // Checking the index node in the LeftBlockIndexNodes //
                selectedIndexNode = leftBlockIndexNodes.getIndexData().get(i);
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
                    if ((selectedIndexNode.getData().getIndex().compareTo(rnIndex) <= 0)) {
                        indexChosenLocationNumber = i;
                        break;
                    }
                    if ((leftNeighbourOfselectedIndexNode.getData().getIndex().compareTo(rnIndex) < 0) && (selectedIndexNode.getData().getIndex().compareTo(rnIndex) > 0)) {
                        indexChosenLocationNumber = i - 1;
                        break;
                    }
                }
            }
            // Initialize the node that the index will proceed to search at next //
            selectedIndexNode = leftBlockIndexNodes.getIndexData().get(indexChosenLocationNumber);
            blockOfIndexesToReturn = traverseTillLastIndexBlock(rnIndex, selectedIndexNode, currentlyIn);
        }
        // Used when there's more than 1 level of BlockOfIndexNodes on the tree //
        // If the index to be searched is greater than the value of the selected indexNode, go to its right pointer (which points to a BlockOfIndexNodes) //
        if ((rnIndex.compareTo(rootIndex) > 0) && (root.getRight() != null) && (root.getRightBlock() == null)) {
            // Set the block of index nodes that it is currently in //
            currentlyIn = root.getRight();
            BlockOfIndexNodes rightBlockIndexNodes = root.getRight();
            // Set the index to be 0 if all the if conditions are not passed //
            int indexChosenLocationNumber = 0;
            // The next IndexNode that the function would traverse to //
            IndexNode selectedIndexNode = null;
            // Goes through each IndexNode in the BlockOfIndexNode that the pointer selects //
            for (int i = rightBlockIndexNodes.size() - 1; i >= 0; i--) {
                // Checking the index node in the RightBlockIndexNodes //
                selectedIndexNode = rightBlockIndexNodes.getIndexData().get(i);
                // If the index that we are searching for has a bigger index compare to all elements in the RightBlockIndexNodes //
                // e.g. rnIndex = G, RightBlockIndexNodes = [A, B, F], since rnIndex is greater than all of them, it should go
                // take the F index and go right of it. [THIS EXAMPLE IS JUST USED FOR A REPRESENTATION]
                if (rnIndex.compareTo(rightBlockIndexNodes.getIndexData().get(rightBlockIndexNodes.size() - 1).getData().getIndex()) > 0) {
                    indexChosenLocationNumber = rightBlockIndexNodes.size() - 1;
                    break;
                }
                // If the element in the LeftBlockIndexNodes is less or equal value compared to the index that we are searching,
                // we will use the element to proceed //
                // e.g. rnIndex = H, LeftBlockIndexNodes = [A, B, C, G, I], we will take element G to proceed [THIS EXAMPLE IS JUST
                // USED FOR A REPRESENTATION] //
                if (i != 0) {
                    IndexNode rightNeighbourOfselectedIndexNode = rightBlockIndexNodes.getIndexData().get(i - 1);
                    if ((selectedIndexNode.getData().getIndex().compareTo(rnIndex) <= 0)) {
                        indexChosenLocationNumber = i;
                        break;
                    }
                    if ((rightNeighbourOfselectedIndexNode.getData().getIndex().compareTo(rnIndex) < 0) && (selectedIndexNode.getData().getIndex().compareTo(rnIndex) > 0)) {
                        indexChosenLocationNumber = i - 1;
                        break;
                    }
                }
            }
            // Initialize the node that the index will proceed to search at next //
            selectedIndexNode = rightBlockIndexNodes.getIndexData().get(indexChosenLocationNumber);
            blockOfIndexesToReturn = traverseTillLastIndexBlock(rnIndex, selectedIndexNode, currentlyIn);
        }
//        System.out.println("currentlyInReachLast2 " + currentlyIn.toString());
        // If it reaches the 1 level above the root, and the index of the IndexNode is lesser or equal to the index to be searched
        // and if there's a BlockOfRecordNode that the IndexNode is pointing to, return the BlockOfRecordNode.
        if ((rnIndex.compareTo(rootIndex) <= 0) && (root.getLeft() == null) && (root.getLeftBlock() != null)) {
            blockOfIndexesToReturn = currentlyIn;
            return currentlyIn;
        }
        // If it reaches the 1 level above the root, and the index of the IndexNode is lesser or equal to the index to be searched
        // and if there's a BlockOfRecordNode that the IndexNode is pointing to, return the BlockOfRecordNode.
        if ((rnIndex.compareTo(rootIndex) > 0) && (root.getRight() == null) && (root.getRightBlock() != null)) {
            blockOfIndexesToReturn = currentlyIn;
            return currentlyIn;
        }
        return blockOfIndexesToReturn;
    }

    // Returning a boolean to indicate the presence of the record, based on its index //
    public boolean search(String indexToSearch) {
        // Boolean to be returned //
        boolean foundTheIndex = false;
        // Used as a parameter for the traverse function. It indicates which IndexNode it should start traversing from //
        IndexNode startTraversingFromThisIndex = null;
        // Get the block of IndexNodes from the highest level of the tree //
        BlockOfIndexNodes highestBlockOfIndex = index.get(0);
        // Serve as a counter. If the BlockOfIndexNodes goes by this [ A, B, C ], we put this counter on the very end (C)
        int startAtIndexNode = highestBlockOfIndex.size() - 1;
        for (int i = 0; i < highestBlockOfIndex.size(); i++) {
            String indexOfIndexNodeInHighestBlock = highestBlockOfIndex.getIndexData().get(i).getData().getIndex();
            if (i > 0) {
                // Going through the indexes in the highest block of the tree //
                String leftNeighbourIndexOfIndexNodeInHighestBlock = highestBlockOfIndex.getIndexData().get(i - 1).getData().getIndex();
                /* If the index (being checked) on the highest block of tree is greater and the left neighbour index of index (being checked) has a smaller value
                 * compared to the search index, return the left neighbour index of index (being checked)
                 * e.g. Block: [ A D ] & rn: B, D > B && A < B, then take the A index.
                 */
                if ((indexOfIndexNodeInHighestBlock.compareTo(indexToSearch) < 0) && (leftNeighbourIndexOfIndexNodeInHighestBlock.compareTo(indexToSearch) > 0)) {
                    startAtIndexNode = i - 1;
                    break;
                }
            }
            if (indexOfIndexNodeInHighestBlock.compareTo(indexToSearch) >= 0) {
                startAtIndexNode = i;
                break;
            }
        }
        // Get the other parameter, which is to indicate which index node from the highest level of tree to start with.
        // The highestBlockOfIndex parameter is given to show the last BlockOfIndexNode the function visits.
        startTraversingFromThisIndex = highestBlockOfIndex.getIndexData().get(startAtIndexNode);
        BlockOfRecordNodes indexToSearchBelongsTo = traverse(indexToSearch, startTraversingFromThisIndex);

        // From the block of records where the index is supposed to be stored at. If the index matches with the one that
        // have existed in the root, return true //
        RecordNode printOutTheBlock = null;
        for (int i = 0; i < indexToSearchBelongsTo.size(); i++) {
            if (indexToSearchBelongsTo.getBlockData().get(i).getData().getIndex().equals(indexToSearch)) {
                foundTheIndex = true;
                printOutTheBlock = indexToSearchBelongsTo.getBlockData().get(i);
            }
        }
        return foundTheIndex;
    }

    // Restructure the tree (For both block of records and block of indexes of the tree) //
    public void restructure() {
        for (int i = 0; i < rootBlocks.size(); i++) {
            // Two block of records that are to be created when a records it to be added into a block and the size of
            // a block of record exceeds its maximum capacity
            BlockOfRecordNodes blockSplitPart1 = new BlockOfRecordNodes();
            BlockOfRecordNodes blockSplitPart2 = new BlockOfRecordNodes();
            // If the block of record size is greater than the maximum capacity
            if (rootBlocks.get(i).size() > maximumSize) {
                // Take in the block that will be restructured
                BlockOfRecordNodes blockToBeRestructured = rootBlocks.get(i);
                // Take the data stored in the block
                ArrayList<RecordNode> blockToBeRestructuredData = blockToBeRestructured.getBlockData();
                // The index of the middle index inside the block
                int indexOfMiddle = -1;
                // If the size is 2, take in the middle index. e.g [ A B C ], take the B to be split into [A B] and [C]
                if (maximumSize == 2) {
                    indexOfMiddle = (blockToBeRestructured.size() / 2);
                }
                // If the size is 3, take in the middle (for odd size) or the left middle (for even size) index.
                // e.g: Max: 5 [ A B C D E F ], take the C to be split into [A B C] and [D E F]
                // e.g. Max: 4 [ A B C D E ], take the B to be split into [A B] and [C D E]
                if (maximumSize > 2) {
                    indexOfMiddle = (blockToBeRestructured.size() / 2) - 1;
                }
                // Take in the record node of the middle record within the block of record to be restructured
                RecordNode middleNode = blockToBeRestructuredData.get(indexOfMiddle);
                // Put in the first half of the block (including the middle record) on the first new block
                for (int j = 0; j <= indexOfMiddle; j++) {
                    blockSplitPart1.addNode(blockToBeRestructuredData.get(j));
                }
                // Set the right neighbour of the first block with the second block
                blockSplitPart1.setNeighbourRight(blockSplitPart2);
                // Put in the second half of the block (without the middle record) on the second new block
                for (int k = (1 + indexOfMiddle); k < blockToBeRestructured.size(); k++) {
                    blockSplitPart2.addNode(blockToBeRestructuredData.get(k));
                }
                // If currently there is no index to traverse to on the tree //
                if (index.isEmpty()) {
                    // Create the first block of index for the tree //
                    BlockOfIndexNodes firstIndexBlock = new BlockOfIndexNodes();
                    // Add the content of the middle record into an index node //
                    IndexNode appendedIndexNode = new IndexNode(middleNode.getData(), blockSplitPart1, blockSplitPart2);
                    // Add the content of the middle record into the first block of the index of the tree //
                    firstIndexBlock.addNode(appendedIndexNode);
                    // Set the index block created as the upper index block of the record blocks
                    blockSplitPart1.setParentIndexBlock(firstIndexBlock);
                    blockSplitPart2.setParentIndexBlock(firstIndexBlock);
                    // Add the first block of index into the index array //
                    index.add(firstIndexBlock);
                }
                // If there is an index on the tree //
                else {
                    // Goes through a function that returns which block of index nodes does the first split block of record nodes last visits
                    BlockOfIndexNodes blockSplitPart1Parent = searchWhichIndexBlockLastVisit(blockSplitPart1.getBlockData().get(blockSplitPart1.size() - 1));
                    // Set the index (parent) block as the upper index block of first split block
                    blockSplitPart1.setParentIndexBlock(blockSplitPart1Parent);
                    // Goes through a function that returns which block of index nodes does the second split block of record nodes last visits
                    BlockOfIndexNodes blockSplitPart2Parent = searchWhichIndexBlockLastVisit(blockSplitPart2.getBlockData().get(blockSplitPart2.size() - 1));
                    // Set the index (parent) block as the upper index block of second split block
                    blockSplitPart2.setParentIndexBlock(blockSplitPart2Parent);
                    // Getting the index (parent) block of the block of record that exceed the maximum capacity
                    int indexOfParentInIndexVar = index.indexOf(blockToBeRestructured.getParentIndexBlock());
//                    System.out.println("restructureindex: " + restructureIndex());
//                    System.out.println("INDEX: " + index.toString());
//                    System.out.println("index size: " + index.size());
//                    System.out.println("");
                    if (index.size() > 2) {
//                        System.out.println("index.get(1) " + index.get(1));
//                        System.out.println("blockToBeRestructured.getParentIndexBlock() " + blockToBeRestructured.getParentIndexBlock().toString());
                    }


                    BlockOfIndexNodes indexBlockOfRestructuredRecordBlock = index.get(indexOfParentInIndexVar);
                    // Add the content of the middle record into an index node //
                    IndexNode appendedIndexNode = new IndexNode(middleNode.getData(), blockSplitPart1, blockSplitPart2);
                    // Add the index node (that has the middle record) into the index (parent) block of the block of record
                    // that exceed the maximum capacity
                    indexBlockOfRestructuredRecordBlock.addNode(appendedIndexNode);
                    // Set the right neighbour of the newly added index, in its block of index, to point to the new added index
                    // on its left pointer

                    if ((indexBlockOfRestructuredRecordBlock.getIndexData().size() >= 2) && (indexBlockOfRestructuredRecordBlock.getIndexData().indexOf(appendedIndexNode) != indexBlockOfRestructuredRecordBlock.getIndexData().size() - 1)) {
                        int indexOfRightIndex = indexBlockOfRestructuredRecordBlock.getIndexData().indexOf(appendedIndexNode) + 1;
                        indexBlockOfRestructuredRecordBlock.getIndexData().get(indexOfRightIndex).setLeftPointer(appendedIndexNode.getRightBlock());
                    }
                    // Set the left neighbour of the newly added index, in its block of index, to point to the new added index
                    // on its right pointer
                    if ((indexBlockOfRestructuredRecordBlock.getIndexData().size() > 1) && (indexBlockOfRestructuredRecordBlock.getIndexData().indexOf(appendedIndexNode) != 0)) {
                        int indexOfLeftIndex = indexBlockOfRestructuredRecordBlock.getIndexData().indexOf(appendedIndexNode) - 1;
                        indexBlockOfRestructuredRecordBlock.getIndexData().get(indexOfLeftIndex).setRightPointer(appendedIndexNode.getLeftBlock());
                    }
//                    System.out.println(" ------------------------- ");
//                    System.out.println("RESTRUCTURE INDEX ??? " + restructureIndex());
//                    System.out.println(" ------------------------- ");
                    // Call upon the function to restructure the index
                    restructureIndex();
                }
                // Remove the record block that exceeds the maximum capacity, and add the 2 newly created block of records into
                // the root array.
                rootBlocks.remove(i);
                rootBlocks.add(i, blockSplitPart2);
                rootBlocks.add(i, blockSplitPart1);
            }
        }
    }

    // Restructure the index tree of the tree //
    public boolean restructureIndex() {
        // The returned value that will indicate if the index tree is balanced
        boolean returnBool = false;
        // Counter for the while loop
        int i = index.size() - 1;
        // Goes through the index over and over again until all the index tree is balance
        while (i > -1) {
            // Two block of index nodes that are to be created when a block of index exceed its maximum capacity
            BlockOfIndexNodes blockIndexSplitPart1 = new BlockOfIndexNodes();
            BlockOfIndexNodes blockIndexSplitPart2 = new BlockOfIndexNodes();
            // If the block of index size is greater than the maximum capacity
            if (index.get(i).size() > maximumSize) {
                // Indicate that the index tree needs to be restructured
                returnBool = true;
                // Take in the index block that will be restructured
                BlockOfIndexNodes blockOfIndexToBeRestructured = index.get(i);
                // Get the upper block (parent) of the block of index that exceeds the maximum capacity size
                BlockOfIndexNodes upperBlockOfBlockOfIndexToBeRestructured = blockOfIndexToBeRestructured.getUpperBlock();

                // A counter used to insert back the block of record that the index block to be restructured is pointing to
                boolean counterToInsertBackBlockOfRecord = false;

                boolean counterToInsertBackBlockOfIndex = false;

                // If the index to be changed is the 1 level above the root (record of node), the block of records need to
                // traverse the updated index structure to know where it belongs on the updated index structure
                ArrayList<BlockOfRecordNodes> blockOfRecordsNodesHoldByTheBlockOfIndex = new ArrayList<>();
                // Taking all the block of records that the index block to be restructured is pointing to
                for (int k = 0; k < blockOfIndexToBeRestructured.getIndexData().size(); k++) {
                    IndexNode indexNodeSelected = blockOfIndexToBeRestructured.getIndexData().get(k);
                    if (indexNodeSelected.getLeftBlock() != null || indexNodeSelected.getRightBlock() != null) {
                        counterToInsertBackBlockOfRecord = true;
                        if ((indexNodeSelected.getLeftBlock() != null)) {
                            if (blockOfRecordsNodesHoldByTheBlockOfIndex.indexOf(indexNodeSelected.getLeftBlock()) < 0) {
                                blockOfRecordsNodesHoldByTheBlockOfIndex.add(indexNodeSelected.getLeftBlock());
                            }
                        }
                        if ((indexNodeSelected.getRightBlock() != null)) {
                            if (blockOfRecordsNodesHoldByTheBlockOfIndex.indexOf(indexNodeSelected.getRightBlock()) < 0) {
                                blockOfRecordsNodesHoldByTheBlockOfIndex.add(indexNodeSelected.getRightBlock());
                            }
                        }
                    }
                }
                //
//                ArrayList<BlockOfIndexNodes> blockOfIndexNodesHoldByTheBlockOfIndex = new ArrayList<>();
//                // Taking all the block of records that the index block to be restructured is pointing to
//                for (int k = 0; k < blockOfIndexToBeRestructured.getIndexData().size(); k++) {
//                    IndexNode indexNodeSelected = blockOfIndexToBeRestructured.getIndexData().get(k);
//                    if (indexNodeSelected.getLeft() != null || indexNodeSelected.getRight() != null) {
//                        counterToInsertBackBlockOfIndex = true;
//                        if ((indexNodeSelected.getLeft() != null)) {
//                            blockOfIndexNodesHoldByTheBlockOfIndex.add(indexNodeSelected.getLeft());
//                        }
//                        if ((indexNodeSelected.getRight() != null)) {
//                            blockOfIndexNodesHoldByTheBlockOfIndex.add(indexNodeSelected.getRight());
//                        }
//                    }
//                }
                // Take the data stored in the index block
                ArrayList<IndexNode> blockOfIndexToBeRestructuredData = blockOfIndexToBeRestructured.getIndexData();
                // The index of the middle index inside the block
                int indexOfMiddle = -1;
                // If the size is 2, take in the middle index. e.g [ A B C ], take the B to be split into [A B] and [C]
                if (maximumSize == 2) {
                    indexOfMiddle = (blockOfIndexToBeRestructured.size() / 2);
                }
                // If the size is 3, take in the middle (for odd size) or the left middle (for even size) index.
                // e.g: Max: 5 [ A B C D E F ], take the C to be split into [A B C] and [D E F]
                // e.g. Max: 4 [ A B C D E ], take the B to be split into [A B] and [C D E]
                if (maximumSize > 2) {
                    indexOfMiddle = (blockOfIndexToBeRestructured.size() / 2) - 1;
                }
                // Take in the record node of the middle record within the block of index to be restructured
                IndexNode middleIndexNode = blockOfIndexToBeRestructuredData.get(indexOfMiddle);
                // Put in the first half of the block (including the middle record) on the first new block
                for (int j = 0; j <= indexOfMiddle; j++) {
                    // Use to not let the right pointer of the first split block to point towards left pointer of the second split block
                    // e.g. [C D I] -> Upper: [D], Lower: [C D] [I], make sure index D does not point on the left pointer of index I from
                    // the other split block
                    blockIndexSplitPart1.addNode(blockOfIndexToBeRestructuredData.get(j));
//                    if (j == indexOfMiddle){
//                        blockIndexSplitPart1.getIndexData().get(blockIndexSplitPart1.size()-1).setRightPointer(new BlockOfIndexNodes());
//                    }
                }
                // Put in the second half of the block (without the middle record) on the second new block
                for (int k = (1 + indexOfMiddle); k < blockOfIndexToBeRestructured.size(); k++) {
                    blockIndexSplitPart2.addNode(blockOfIndexToBeRestructuredData.get(k));
                }
                // Create an upper block (parent) block of indexes for the 2 new block of indexes that are split
                BlockOfIndexNodes upperLevelIndexNode = new BlockOfIndexNodes();
                // Add the content of the middle record into an index node //
                IndexNode appendedIndexNode = new IndexNode(middleIndexNode.getData(), blockIndexSplitPart1, blockIndexSplitPart2);
                // Add the index node (that has the middle record) into the index (parent) block that is created
                upperLevelIndexNode.addNode(appendedIndexNode);
                // Set the upper block (parent) block
                blockIndexSplitPart1.setUpperBlock(upperLevelIndexNode);
                blockIndexSplitPart2.setUpperBlock(upperLevelIndexNode);

                // Should be able to be deleted?
                // If in the case that the block of index to be restructured is located 1 level above root, set the
                // 2 newly split block of indexes pointing towards an empty block of records
                BlockOfRecordNodes newBlockOfRecordNodes = new BlockOfRecordNodes();
                if ((blockIndexSplitPart1.getIndexData().get(0).getLeftBlock() != null) || (blockIndexSplitPart1.getIndexData().get(0).getRightBlock() != null)) {
                    blockIndexSplitPart1.getIndexData().get(blockIndexSplitPart1.size() - 1).setRightPointer(newBlockOfRecordNodes);
                }
                if ((blockIndexSplitPart2.getIndexData().get(0).getLeftBlock() != null) || (blockIndexSplitPart2.getIndexData().get(0).getRightBlock() != null)) {
                    blockIndexSplitPart2.getIndexData().get(0).setLeftPointer(newBlockOfRecordNodes);
                }

                // If the upper block (parent) block of the index block to be restructured is none (which usually means it's the highest part of the index tree)
                if (upperBlockOfBlockOfIndexToBeRestructured == null) {
                    // Set each of the index within the parent of the index block to be restructured to NOT point towards any block of records
                    for (int b = 0; b < upperLevelIndexNode.size() - 1; b++) {
                        BlockOfRecordNodes makeItNull = null;
                        upperLevelIndexNode.getIndexData().get(b).setLeftPointer(makeItNull);
                        upperLevelIndexNode.getIndexData().get(b).setRightPointer(makeItNull);
                    }
                    // Add the 2 newly split block of indexes into the array of indexes
                    index.add(0, blockIndexSplitPart2);
                    index.add(0, blockIndexSplitPart1);
                    // Add the newly created upper block (parent) of the 2 newly split index blocks into the array of indexes
                    index.add(0, upperLevelIndexNode);

                    /* Going through the whole indexes, if the parent of an index block is the index of block to be restructured,
                     * update the parent according to the 2 newly split index blocks. Any block that is match will take the very
                     * right of its index and be compared to the 2 newly split index blocks.
                     * e.g. Max size: 2, Index to be restructured is: [D E F] and it is split into [D E] and [F] with [E] as the
                     * upper block of the 2 split blocks.
                     * Any index block with the parent of [D E F] must be updated with the change. If let's say, [A D] is on of the
                     * records that its parent is [D E F]. [A D] parent/upper block needs to be updated from [D E F] to [D E]
                     */
                    for (int z = 0; z < index.size() - 1; z++) {
                        BlockOfIndexNodes blockOfIndexBeingChecked = index.get(z);
                        BlockOfIndexNodes upperOfBlockOfIndexBeingChecked = blockOfIndexBeingChecked.getUpperBlock();
                        if ((upperOfBlockOfIndexBeingChecked != null) && (upperOfBlockOfIndexBeingChecked.equals(blockOfIndexToBeRestructured))) {
                            String endIndexOfBlock = blockOfIndexBeingChecked.getIndexData().get(blockOfIndexBeingChecked.size() - 1).getData().getIndex();
                            String endIndexOfBlockOfBlockSplit1 = blockIndexSplitPart1.getIndexData().get(blockIndexSplitPart1.size() - 1).getData().getIndex();
                            // Taking the example from before, if index D from block [A D] is smaller or equal to end of block split 1 [D E],
                            // update the parent of [A D] into [D E]
                            if (endIndexOfBlock.compareTo(endIndexOfBlockOfBlockSplit1) <= 0) {
                                blockOfIndexBeingChecked.setUpperBlock(blockIndexSplitPart1);
                            } else {
                                blockOfIndexBeingChecked.setUpperBlock(blockIndexSplitPart2);
                            }
                        }
                    }
                    // Remove the index to be restructured from the array of indexes
                    index.remove(blockOfIndexToBeRestructured);
                }
                // If the upper block (parent) block of the index block to be restructured is another block of index
                else {
                    // Remove the parent of the block of index to be restructured from the array of indexes
                    index.remove(upperBlockOfBlockOfIndexToBeRestructured);
                    // Add the middle index node into the parent of the block of index to be restructured
                    upperBlockOfBlockOfIndexToBeRestructured.addNode(appendedIndexNode);
                    // Set the upper block (parent) of the 2 newly split blocks of indexes to the updated parent
                    blockIndexSplitPart1.setUpperBlock(upperBlockOfBlockOfIndexToBeRestructured);
                    blockIndexSplitPart2.setUpperBlock(upperBlockOfBlockOfIndexToBeRestructured);

                    // Initialize the left and right pointer of the index node that is recently appended into the parent block
                    // of the index block to be restructured with the other index nodes that have existed within it.
                    int indexOfAppendedIndexNode = upperBlockOfBlockOfIndexToBeRestructured.getIndexData().indexOf(appendedIndexNode);
                    if (indexOfAppendedIndexNode > 0) {
                        upperBlockOfBlockOfIndexToBeRestructured.getIndexData().get(indexOfAppendedIndexNode - 1).setRightPointer(blockIndexSplitPart1);
                    }
                    if (indexOfAppendedIndexNode < upperBlockOfBlockOfIndexToBeRestructured.size() - 1) {
                        upperBlockOfBlockOfIndexToBeRestructured.getIndexData().get(indexOfAppendedIndexNode + 1).setLeftPointer(blockIndexSplitPart2);
                    }
                    for (int b = 0; b < blockIndexSplitPart1.size(); b++) {
                        blockIndexSplitPart1.getIndexData().get(b);
                    }
                    // Set each of the index within the parent of the index block to be restructured to NOT point towards any block of records
                    for (int b = 0; b < upperBlockOfBlockOfIndexToBeRestructured.size() - 1; b++) {
                        BlockOfRecordNodes makeItNull = null;
                        upperBlockOfBlockOfIndexToBeRestructured.getIndexData().get(b).setLeftPointer(makeItNull);
                        upperBlockOfBlockOfIndexToBeRestructured.getIndexData().get(b).setRightPointer(makeItNull);
                    }
                    // Add the second part of the split index blocks into the array of indexes
                    index.add(0, blockIndexSplitPart2);
                    // Add the first part of the split index blocks into the array of indexes
                    index.add(0, blockIndexSplitPart1);
                    // Add the upper block (parent) of the 2 split index blocks into the array of indexes
                    index.add(0, upperBlockOfBlockOfIndexToBeRestructured);
                    /* Going through the whole indexes, if the parent of an index block is the index of block to be restructured,
                     * update the parent according to the 2 newly split index blocks. Any block that is match will take the very
                     * right of its index and be compared to the 2 newly split index blocks.
                     * e.g. Max size: 2, Index to be restructured is: [D E F] and it is split into [D E] and [F] with [E] as the
                     * upper block of the 2 split blocks.
                     * Any index block with the parent of [D E F] must be updated with the change. If let's say, [A D] is on of the
                     * records that its parent is [D E F]. [A D] parent/upper block needs to be updated from [D E F] to [D E]
                     */
                    for (int z = 0; z < index.size() - 1; z++) {
                        BlockOfIndexNodes blockOfIndexBeingChecked = index.get(z);
                        BlockOfIndexNodes upperOfBlockOfIndexBeingChecked = blockOfIndexBeingChecked.getUpperBlock();
                        if ((upperOfBlockOfIndexBeingChecked != null) && (upperOfBlockOfIndexBeingChecked.equals(blockOfIndexToBeRestructured))) {
                            String endIndexOfBlock = blockOfIndexBeingChecked.getIndexData().get(blockOfIndexBeingChecked.size() - 1).getData().getIndex();
                            String endIndexOfBlockOfBlockSplit1 = blockIndexSplitPart1.getIndexData().get(blockIndexSplitPart1.size() - 1).getData().getIndex();
                            // Taking the example from before, if index D from block [A D] is smaller or equal to end of block split 1 [D E],
                            // update the parent of [A D] into [D E]
                            if (endIndexOfBlock.compareTo(endIndexOfBlockOfBlockSplit1) <= 0) {
                                blockOfIndexBeingChecked.setUpperBlock(blockIndexSplitPart1);
                            } else {
                                blockOfIndexBeingChecked.setUpperBlock(blockIndexSplitPart2);
                            }
                        }
                    }
//                    for (int z = 0; z < rootBlocks.size() - 1; z++){
//                        BlockOfRecordNodes blockOfRecordsBeingChecked = rootBlocks.get(z);
//                        BlockOfIndexNodes upperOfBlockOfRecordsBeingChecked = blockOfRecordsBeingChecked.getParentIndexBlock();
//                        if ((upperOfBlockOfRecordsBeingChecked != null) && (upperOfBlockOfRecordsBeingChecked.equals(blockOfIndexToBeRestructured))) {
//                            String endIndexOfBlock = blockOfRecordsBeingChecked.getBlockData().get(blockOfRecordsBeingChecked.size() - 1).getData().getIndex();
//                            String endIndexOfBlockOfBlockSplit1 = blockIndexSplitPart1.getIndexData().get(blockIndexSplitPart1.size() - 1).getData().getIndex();
//                            // Taking the example from before, if index D from block [A D] is smaller or equal to end of block split 1 [D E],
//                            // update the parent of [A D] into [D E]
//                            if (endIndexOfBlock.compareTo(endIndexOfBlockOfBlockSplit1) <= 0) {
//                                blockOfRecordsBeingChecked.setParentIndexBlock(blockIndexSplitPart1);
//                            } else {
//                                blockOfRecordsBeingChecked.setParentIndexBlock(blockIndexSplitPart2);
//                            }
//                        }
//                    }
                    // Remove the index to be restructured from the array of indexes
                    index.remove(blockOfIndexToBeRestructured);
                }
                placeTheTopIndexCorrectly();
                // If there is block of records that are being held by the index to be restructured that need to be inserted back
                if (counterToInsertBackBlockOfRecord == true) {
                    // Delete any block of record nodes that are pointed by the indexes stored in the 2 split blocks of indexes
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
                    // Insert back the block of records hold by the block of index that is restructured
                    for (int c = 0; c < blockOfRecordsNodesHoldByTheBlockOfIndex.size(); c++) {
                        BlockOfRecordNodes blockOfRecordToBeAdded = blockOfRecordsNodesHoldByTheBlockOfIndex.get(c);
                        if (blockOfRecordToBeAdded.getBlockData().size() > 0) {
                            // Take in the very right index of the record to be used for the traverse till last block of index founded function
                            // e.g. Very right index of record [ A B C] is C
                            RecordNode veryRightOfBlockOfRecordToBeAdded = blockOfRecordToBeAdded.getBlockData().get(blockOfRecordToBeAdded.size() - 1);
                            String veryRightIndexOfBlockOfRecordToBeAdded = veryRightOfBlockOfRecordToBeAdded.getData().getIndex().toString();
                            String endIndexOfBlockOfBlockSplit1 = blockIndexSplitPart1.getIndexData().get(blockIndexSplitPart1.size() - 1).getData().getIndex();
                            // Taking the example from before, if index D from block [A D] is smaller or equal to end of block split 1 [D E],
                            // update the parent of [A D] into [D E]
                            BlockOfIndexNodes updatedParentOfBlockOfRecord = null;
                            if (veryRightIndexOfBlockOfRecordToBeAdded.compareTo(endIndexOfBlockOfBlockSplit1) <= 0) {
                                blockOfRecordToBeAdded.setParentIndexBlock(blockIndexSplitPart1);
                                updatedParentOfBlockOfRecord = blockIndexSplitPart1;
                            } else {
                                blockOfRecordToBeAdded.setParentIndexBlock(blockIndexSplitPart2);
                                updatedParentOfBlockOfRecord = blockIndexSplitPart2;
                            }
                            if (updatedParentOfBlockOfRecord.size() > maximumSize) {
                                System.exit(0);
                            }
                            for (int e = updatedParentOfBlockOfRecord.size() - 1; e >= 0; e--) {
                                IndexNode indexChecked = updatedParentOfBlockOfRecord.getIndexData().get(e);
                                // Update the correct upper block (parent) for the given record block

//                                System.out.println(" ********************************************** ");
//                                System.out.println("indexChecked " + indexChecked.toStringJustIndex());
//                                System.out.println("blockOfRecordToBeAdded " + blockOfRecordToBeAdded.toString());
//                                System.out.println("updatedParentOfBlockOfRecord " + updatedParentOfBlockOfRecord.toString());
//                                System.out.println(" ********************************************** ");
                                if (e != 0) {
                                    IndexNode leftIndexChecked = updatedParentOfBlockOfRecord.getIndexData().get(e - 1);
                                    // If it has a greater value than the index that are within the block of index
                                    // e.g. [ A B C ], D would be the one that the C right pointer points to
                                    if ((e == updatedParentOfBlockOfRecord.size() - 1) && (indexChecked.getData().getIndex().compareTo(veryRightIndexOfBlockOfRecordToBeAdded) < 0)) {
                                        indexChecked.setRightPointer(blockOfRecordToBeAdded);
                                        break;
                                    }
                                    // e.g. [ A B D ], C would be the one that D left pointer points to and what B right pointer points to
                                    if ((indexChecked.getData().getIndex().compareTo(veryRightIndexOfBlockOfRecordToBeAdded) <= 0)) {
                                        indexChecked.setLeftPointer(blockOfRecordToBeAdded);
                                        leftIndexChecked.setRightPointer(blockOfRecordToBeAdded);
                                        break;
                                    }
                                }
                                if (e == 0) {
                                    // e.g. [ M ], E would be the one that M left pointer points
                                    if (indexChecked.getData().getIndex().compareTo(veryRightIndexOfBlockOfRecordToBeAdded) >= 0) {
                                        indexChecked.setLeftPointer(blockOfRecordToBeAdded);
                                        break;
                                    }
                                    // e.g. [ M ], N would be the one that M left pointer points
                                    else {
                                        indexChecked.setRightPointer(blockOfRecordToBeAdded);
                                        if (updatedParentOfBlockOfRecord.size() > 1) {
                                            IndexNode rightIndexChecked = updatedParentOfBlockOfRecord.getIndexData().get(e + 1);
                                            rightIndexChecked.setLeftPointer(blockOfRecordToBeAdded);
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    blockOfRecordsNodesHoldByTheBlockOfIndex.clear();
                }
                // Restart the loop again to make sure all indexes are balance
                i = index.size() - 1;
            } else {
                i--;
            }
        }
        return returnBool;
    }

    // Add the record block into the root of the tree
    public void addBlock(BlockOfRecordNodes newBlock) {
        this.rootBlocks.add(newBlock);
    }

    public void placeTheTopIndexCorrectly() {
        BlockOfIndexNodes blockOfIndexNodesAtTheTopOfTree = null;
        int indexOfBlockOfIndexNodesAtTheTopOfTree = -1;
        for (int i = 0; i < index.size(); i++) {
            if (index.get(i).getUpperBlock() == null) {
                indexOfBlockOfIndexNodesAtTheTopOfTree = i;
                break;
            }
        }
        blockOfIndexNodesAtTheTopOfTree = index.get(indexOfBlockOfIndexNodesAtTheTopOfTree);
        index.remove(indexOfBlockOfIndexNodesAtTheTopOfTree);
        index.add(0, blockOfIndexNodesAtTheTopOfTree);
    }

    // Change the string's format so it's visible for debugging
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

