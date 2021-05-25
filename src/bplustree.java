import java.util.ArrayList;

public class bplustree {
    public static void main(String[] args) {
        BPlusTreeIndex root = new BPlusTreeIndex(10);

        RecordNode recordNodeA = new RecordNode();
        ArrayList<String> valueA = new ArrayList<>();
        valueA.add("1");
        valueA.add("Data content A");
        IndexAndDataStored recordA = new IndexAndDataStored("A", valueA);
        recordNodeA.setData(recordA);

        RecordNode recordNodeB = new RecordNode();
        ArrayList<String> valueB = new ArrayList<>();
        valueB.add("1");
        valueB.add("Data content B");
        IndexAndDataStored recordB = new IndexAndDataStored("A", valueB);
        recordNodeB.setData(recordB);

        ArrayList<String> valueC = new ArrayList<>();
        valueC.add("1");
        valueC.add("Data content C");
        RecordNode recordNodeC = new RecordNode();
        IndexAndDataStored recordC = new IndexAndDataStored("C", valueC);
        recordNodeC.setData(recordC);

        ArrayList<String> valueD = new ArrayList<>();
        valueD.add("1");
        valueD.add("Data content D");
        RecordNode recordNodeD = new RecordNode();
        IndexAndDataStored recordD = new IndexAndDataStored("D", valueD);
        recordNodeD.setData(recordD);

        ArrayList<String> valueE = new ArrayList<>();
        valueE.add("1");
        valueE.add("Data content E");
        RecordNode recordNodeE = new RecordNode();
        IndexAndDataStored recordE = new IndexAndDataStored("E", valueE);
        recordNodeE.setData(recordE);

        ArrayList<String> valueF = new ArrayList<>();
        valueF.add("1");
        valueF.add("Data content F");
        RecordNode recordNodeF = new RecordNode();
        IndexAndDataStored recordF = new IndexAndDataStored("F", valueF);
        recordNodeF.setData(recordF);

        ArrayList<String> valueG = new ArrayList<>();
        valueG.add("1");
        valueG.add("Data content G");
        RecordNode recordNodeG = new RecordNode();
        IndexAndDataStored recordG = new IndexAndDataStored("G", valueG);
        recordNodeG.setData(recordG);

        ArrayList<String> valueH = new ArrayList<>();
        valueH.add("1");
        valueH.add("Data content H");
        RecordNode recordNodeH = new RecordNode();
        IndexAndDataStored recordH = new IndexAndDataStored("H", valueH);
        recordNodeH.setData(recordH);

        ArrayList<String> valueI = new ArrayList<>();
        valueI.add("1");
        valueI.add("Data content I");
        RecordNode recordNodeI = new RecordNode();
        IndexAndDataStored recordI = new IndexAndDataStored("I", valueI);
        recordNodeI.setData(recordI);

        ArrayList<String> valueJ = new ArrayList<>();
        valueJ.add("1");
        valueJ.add("Data content J");
        RecordNode recordNodeJ = new RecordNode();
        IndexAndDataStored recordJ = new IndexAndDataStored("J", valueJ);
        recordNodeJ.setData(recordJ);

        ArrayList<String> valueK = new ArrayList<>();
        valueK.add("1");
        valueK.add("Data content K");
        RecordNode recordNodeK = new RecordNode();
        IndexAndDataStored recordK = new IndexAndDataStored("K", valueK);
        recordNodeK.setData(recordK);

        ArrayList<String> valueL = new ArrayList<>();
        valueL.add("1");
        valueL.add("Data content L");
        RecordNode recordNodeL = new RecordNode();
        IndexAndDataStored recordL = new IndexAndDataStored("L", valueL);
        recordNodeL.setData(recordL);

        ArrayList<String> valueM = new ArrayList<>();
        valueM.add("1");
        valueM.add("Data content M");
        RecordNode recordNodeM = new RecordNode();
        IndexAndDataStored recordM = new IndexAndDataStored("M", valueM);
        recordNodeM.setData(recordM);

        ArrayList<String> valueN = new ArrayList<>();
        valueN.add("1");
        valueN.add("Data content N");
        RecordNode recordNodeN = new RecordNode();
        IndexAndDataStored recordN = new IndexAndDataStored("N", valueN);
        recordNodeN.setData(recordN);

        ArrayList<String> valueO = new ArrayList<>();
        valueO.add("1");
        valueO.add("Data content O");
        RecordNode recordNodeO = new RecordNode();
        IndexAndDataStored recordO = new IndexAndDataStored("O", valueO);
        recordNodeO.setData(recordO);

        root.addNode(recordNodeO);
        root.addNode(recordNodeK);
        root.addNode(recordNodeM);
        root.addNode(recordNodeN);
        root.addNode(recordNodeA);
        root.addNode(recordNodeB);
        root.addNode(recordNodeC);
        root.addNode(recordNodeI);
        root.addNode(recordNodeJ);
        root.addNode(recordNodeK);
        root.addNode(recordNodeF);
        root.addNode(recordNodeE);
        root.addNode(recordNodeG);
        root.addNode(recordNodeD);
        root.addNode(recordNodeH);
        long startTime = System.currentTimeMillis();
        System.out.println("Search A: " + root.search("A"));
        System.out.println("Search B: " + root.search("B"));
        System.out.println("Search C: " + root.search("C"));
        System.out.println("Search D: " + root.search("D"));
        System.out.println("Search E: " + root.search("E"));
        System.out.println("Search F: " + root.search("F"));
        System.out.println("Search G: " + root.search("G"));
        System.out.println("Search H: " + root.search("H"));
        System.out.println("Search I: " + root.search("I"));
        System.out.println("Search J: " + root.search("J"));
        System.out.println("Search K: " + root.search("K"));
        System.out.println("Search L: " + root.search("L"));
        System.out.println("Search M: " + root.search("M"));
        System.out.println("Search N: " + root.search("N"));
        System.out.println("Search O: " + root.search("O"));
        System.out.println("Search P: " + root.search("P"));


        long endTime = System.currentTimeMillis();
        System.out.println("Time to search (ms): " + (endTime - startTime) / 1000);

//        firstBlock.addNode(recordNodeA);
//        firstBlock.addNode(recordNodeC);
//        firstBlock.addNode(recordNodeD);
//        System.out.println(firstBlock.toString());
        System.out.println(root.toString());

    }
}

