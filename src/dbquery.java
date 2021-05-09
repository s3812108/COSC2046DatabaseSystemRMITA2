import java.io.*;
import java.math.BigInteger;

public class dbquery {
    final static char newRecordSeparator = '\t';
    final static char newPageSeparator = '\n';

    // Pederson, K. (2010). Converting A String To Hexadecimal In Java. https://stackoverflow.com/questions/923863/converting-a-string-to-hexadecimal-in-java//
    public static String convertToHex(String arg) throws UnsupportedEncodingException {
        return String.format("%x", new BigInteger(1, arg.getBytes("UTF-8")));
    }

    // janos. (2015). Convert Hexadecimal to String. https://stackoverflow.com/questions/32806404/convert-hexadecimal-to-string
    public static String convertToReadableString(String input) {
        byte[] bytes = new byte[input.length() / 2];
        for (int i = 0; i < input.length(); i += 2) {
            bytes[i / 2] = (byte) ((Character.digit(input.charAt(i), 16) << 4) + Character.digit(input.charAt(i + 1), 16));
        }
        return new String(bytes);
    }

    public static void main(String[] args) {

        try {
            // Taking the SDT_NAME, given in the format Sensor_ID + Date_Time from the argument //
            String sdtNameToBeSearched = "";
            for (int i = 0; i < args.length - 1; i++) {
                sdtNameToBeSearched += args[i];
                if (i != (args.length - 2)) {
                    sdtNameToBeSearched += " ";
                }
            }
            int pagesizeLimit = Integer.parseInt(args[args.length - 1]);
            String heapFilePath = "heap." + pagesizeLimit;
            try {
                long startTime = System.currentTimeMillis();
                searchingHeapFile(sdtNameToBeSearched, heapFilePath);
                long endTime = System.currentTimeMillis();
                System.out.println("Time to create (ms): " + (endTime - startTime) / 1000);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    // Code that will search the heap file //
    public static void searchingHeapFile(String textToSearch, String heapFilePath) throws FileNotFoundException {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(heapFilePath));
            // Used to read the whole data //
            String data = null;
            // Convert the SDT_NAME to be searched into hex
            String textToBeSearchConverted = convertToHex(textToSearch);
            int page = 0;

            while ((data = reader.readLine()) != null) {
                page++;
                // Splitting each page of the data by the record separator //
                String[] eachRecord = data.split("\t");
                for (int i = 0; i < eachRecord.length; i++) {
                    // Splitting each row of the data by the comma //
                    String[] eachData = eachRecord[i].split(",");
                    int sdtNameIndex = eachData.length - 1;
                    // If the converted SDT_NAME to be searched can be found in the data //
                    if (textToBeSearchConverted.equals(eachData[sdtNameIndex])) {
                        String readableRecord = "";
                        for (int j = 0; j < eachData.length; j++) {
                            try {
                                // If the data checked is supposedly originating from integer, convert it accordingly //
                                // Séguret, D. (2012). Convert hex string to int. https://stackoverflow.com/questions/11194513/convert-hex-string-to-int //
                                Long integerData = Long.parseLong(eachData[j], 16);
                                readableRecord += integerData;
                            }
                            // The data should've came from a string type, convert it accordingly //
                            catch (Exception e) {
                                readableRecord += convertToReadableString(eachData[j]);
                            }
                            // For each value in a record, put a comma //
                            if (j != eachData.length - 1) {
                                readableRecord += ",";
                            }
                        }
                        System.out.println("On Page " + (page) + ": " + readableRecord + "\n");
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
