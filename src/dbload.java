import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

public class dbload {

    final static char newRecordSeparator = '\t';
    final static char newPageSeparator = '\n';

    // Pederson, K. (2010). Converting A String To Hexadecimal In Java. https://stackoverflow.com/questions/923863/converting-a-string-to-hexadecimal-in-java//
    public static String convertToHex(String arg) throws UnsupportedEncodingException {
        return String.format("%x", new BigInteger(1, arg.getBytes("UTF-8")));
    }

    public static void main(String[] args) {
        // Taking the necessary inputs from the argument //
        try {
            int pagesizeLimit = Integer.parseInt(args[1]);
            String dataName = args[2];
            try {
                long startTime = System.currentTimeMillis();
                writingHeapFile(pagesizeLimit, dataName);
                long endTime = System.currentTimeMillis();
                System.out.println("Time to create (ms): " + (endTime - startTime) / 1000);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    // Code that will write the heap file //
    public static void writingHeapFile(int pagesizeLimit, String dataName) throws FileNotFoundException {
        // Variables used to read and write a file //
        BufferedReader reader;
        File path = new File("heap." + pagesizeLimit);
        OutputStream fileOutputStream = new FileOutputStream(path, true);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
        // ArrayList that will store the directory pages of the data //
        ArrayList<char[]> directoryOfPages = new ArrayList<char[]>();

        try {
            reader = new BufferedReader(new FileReader(dataName));
            // Storing all the accumulated records in the form of chars in a page that will be written once a page is full //
            char[] charArrayInAPage = new char[pagesizeLimit];
            // Initialize all the chars in the page with 0 //
            Arrays.fill(charArrayInAPage, '0');
             /* ArrayList used as a temporary (adjustable) array that will hold the accumulation of records until the
            page is full */
            ArrayList<Character> currentCharInThePage = new ArrayList<>();
            // Variables used to measure the length of the record(s) within a page //
            int counter = 0;
            int numOfPages = 1;
            int numOfRecords = 0;
            // Variables used to assist the integration of SDT_NAME into the output //
            int indexOfSensorID = -1;
            int indexOfDateTime = -1;
            boolean sensorIDAndDateTimeExist = false;
            boolean triggerPutSDTNAME = true;
            // Used to read the whole data //
            String data = null;

            while ((data = reader.readLine()) != null) {
                // A string that will hold the value of all the data in a row that has been converted //
                String eachRowDataAllHexString = "";
                // Splitting each row of the data by the comma //
                String[] eachRowData = data.split(",");
                // Counter used to know if the record is inserted into an existing page //
                boolean appendedToExistingPage = false;

                for (int i = 0; i < eachRowData.length; i++) {
                    if (eachRowData[i].toUpperCase().equals("SENSOR_ID")) {
                        indexOfSensorID = i;
                    } else if (eachRowData[i].toUpperCase().equals("DATE_TIME")) {
                        indexOfDateTime = i;
                    }
                    // Used as a gateway to insert the column header "SDT_NAME" into the first line once //
                    if (indexOfDateTime >= 0 && indexOfSensorID >= 0 && triggerPutSDTNAME == true) {
                        sensorIDAndDateTimeExist = true;
                    }
                    // Appending "," for each data excluding the very first data of the record //
                    if (i > 0) {
                        eachRowDataAllHexString += ",";
                    }
                    // For data that stores only integer value, convert the data into a character type //
                    try {
                        int integerInEachRowData = Integer.parseInt(eachRowData[i]);
                        eachRowDataAllHexString += Integer.toHexString(integerInEachRowData);
                    }
                    // For data that is not of integer type, convert the data into hexadecimal value //
                    catch (Exception e) {
                        eachRowDataAllHexString += convertToHex(eachRowData[i]);
                    }
                    // Adding the SDT_NAME variable to each record //
                    if (sensorIDAndDateTimeExist == true) {
                        if (i == eachRowData.length - 1) {
                            eachRowDataAllHexString += ",";
                            // If the current record is the header of the data, insert the word "SDT_NAME" //
                            if (triggerPutSDTNAME == true) {
                                eachRowDataAllHexString += convertToHex("SDT_NAME");
                                triggerPutSDTNAME = false;
                            } else {
                                eachRowDataAllHexString += convertToHex(eachRowData[indexOfSensorID].concat(eachRowData[indexOfDateTime]));
                            }
                        }
                    }
                }

                // Checking if the record can be inserted into an existing page //
                if (directoryOfPages.size() > 0) {
                    for (int i = 0; i < directoryOfPages.size(); i++) {
                        char[] eachPageCharArray = directoryOfPages.get(i);
                        /* Variables used to identify the number of spaces available in each page and if so, which index
                        will it start with */
                        int emptySpaceIndexStart = -1;
                        int numOfEmptySpace = 0;
                        /* Identify the available spaces by searching through 0s starting from the end to the very first
                        new record separator */
                        for (int j = eachPageCharArray.length - 1; j >= 0; j--) {
                            if (Character.toString(eachPageCharArray[j]).equals("0")) {
                                emptySpaceIndexStart = j;
                                numOfEmptySpace += 1;
                            } else {
                                break;
                            }
                        }

                        // If the given record fits within a page //
                        if (numOfEmptySpace >= eachRowDataAllHexString.length()) {
                            char[] eachRowDataAllHexIntoChar = eachRowDataAllHexString.toCharArray();
                            // Take the page, append the new record into it
                            for (int k = 0; k < eachRowDataAllHexString.length(); k++) {
                                eachPageCharArray[emptySpaceIndexStart] = eachRowDataAllHexIntoChar[k];
                                emptySpaceIndexStart++;
                            }
                            // Overwrite the new modified page into the directory of pages
                            directoryOfPages.set(i, eachPageCharArray);
                            // If a record has done this step, we can skip the process below and move to the next one //
                            appendedToExistingPage = true;
                            break;
                        }
                    }
                }

                // If the record does not fit into any existing pages //
                if (appendedToExistingPage == false) {
                    // If a single page can fit in the record (+ any other records that are accumulated along) //
                    if ((eachRowDataAllHexString.length() + counter) < pagesizeLimit) {
                        // Appending the counter with the length of the record to be inserted into the page //
                        counter += eachRowDataAllHexString.length();
                        // Splitting the converted string of a row into array of char //
                        char[] eachRowDataAllHexIntoChar = eachRowDataAllHexString.toCharArray();
                        // Accumulating the record into the temporary array of records within a page //
                        for (int i = 0; i < eachRowDataAllHexIntoChar.length; i++) {
                            currentCharInThePage.add(eachRowDataAllHexIntoChar[i]);
                        }
                        // Adds a new line for every record inserted into the page //
                        currentCharInThePage.add(newRecordSeparator);
                        counter += 1;
                        numOfRecords += 1;
                    }
                    // If a record (+ any other records that are accumulated along) does not fit within a single page //
                    else {
                        /* Passing on the record (+ any other records that are accumulated along) from the ArrayList of
                        characters into the char[] array that will be inserted into the page. */
                        for (int i = 0; i < currentCharInThePage.size(); i++) {
                            charArrayInAPage[i] = currentCharInThePage.get(i);
                        }
                        String test = "";
                        for (int i = 0; i < charArrayInAPage.length; i++) {
                            test += charArrayInAPage[i];
                        }
                        // Add the accumulated records into a new page in the directory of pages //
                        directoryOfPages.add(charArrayInAPage);
                        numOfPages += 1;

                        // Clearing all the variables used for the previous page and resetting it for the next one //
                        currentCharInThePage.clear();
                        counter = 0;
                        charArrayInAPage = new char[pagesizeLimit];
                        Arrays.fill(charArrayInAPage, '0');

                        // Accumulate the record into the new page, the same procedure as applied in the previous function //
                        counter += eachRowDataAllHexString.length();
                        char[] eachRowDataAllHexIntoChar = eachRowDataAllHexString.toCharArray();
                        for (int i = 0; i < eachRowDataAllHexIntoChar.length; i++) {
                            currentCharInThePage.add(eachRowDataAllHexIntoChar[i]);
                        }
                        currentCharInThePage.add(newRecordSeparator);
                        counter += 1;
                        numOfRecords += 1;
                    }
                }
            }

                /* Since "data = reader.readLine()) != null" does not detect the last record of the data, all the rest
                of the variables that are still haven't been processed
                data */
            boolean finalRecordAppendedToExistingPage = false;
            for (int i = 0; i < currentCharInThePage.size(); i++) {
                charArrayInAPage[i] = currentCharInThePage.get(i);
            }
            if (directoryOfPages.size() > 0) {
                for (int i = 0; i < directoryOfPages.size(); i++) {
                    char[] eachPageCharArray = directoryOfPages.get(i);
                        /* Variables used to identify the number of spaces available in each page and if so, which index
                        will it start with */
                    int emptySpaceIndexStart = -1;
                    int numOfEmptySpace = 0;
                            /* Identify the available spaces by searching through 0s starting from the end to the very first
                            new record separator */
                    for (int j = eachPageCharArray.length - 1; j >= 0; j--) {
                        if (Character.toString(eachPageCharArray[j]).equals("0")) {
                            emptySpaceIndexStart = j;
                            numOfEmptySpace += 1;
                        } else {
                            break;
                        }
                    }
                    // If the given record fits within a page //
                        /* -1 was added because the currentCharInThePage has a record separator in the end, this needs
                        to be removed */
                    if (numOfEmptySpace >= currentCharInThePage.size() - 1) {
                        // Take the page, append the new record into it
                        for (int k = 0; k < currentCharInThePage.size() - 1; k++) {
                            eachPageCharArray[emptySpaceIndexStart] = charArrayInAPage[k];
                            emptySpaceIndexStart++;
                        }
                        // Overwrite the new modified page into the directory of pages
                        directoryOfPages.set(i, eachPageCharArray);
                        finalRecordAppendedToExistingPage = true;
                        break;
                    }
                }
            }
            // If it's not appended into an existing page, create a new page and store it there //
            if (finalRecordAppendedToExistingPage == false) {
                for (int j = 0; j < currentCharInThePage.size(); j++) {
                    charArrayInAPage[j] = currentCharInThePage.get(j);
                }
                directoryOfPages.add(charArrayInAPage);
            }
            /* Writing out the directory of pages into the file */
            for (int i = 0; i < directoryOfPages.size(); i++) {
                char[] eachPageFroDirectory = directoryOfPages.get(i);
                // Flush/write the pages //
                outputStreamWriter.write(eachPageFroDirectory);
                outputStreamWriter.write(newPageSeparator);
            }
            reader.close();
            outputStreamWriter.close();
            fileOutputStream.close();
            // Stdout commands //
            System.out.println("File created: " + path.getPath());
            System.out.println("Number of record(s) loaded: " + numOfRecords);
            System.out.println("Number of page(s) used: " + numOfPages);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
