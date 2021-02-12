package ie.gmit.dip;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PolybiusSquareCipher {

    private static HashMap<Character, String> hashMap = new HashMap<Character, String>();
    private static HashMap<String, Character> reversedHashMap = new HashMap<String, Character>();

    private final String secretWord;
    private int numOfRows;
    private String[] sortedKey;

    /*
     * Filling HashMap in Static block. HasMap is used to assign chars of plainText
     * to two cipher characters. Single static block is used for initialising the
     * static values. The block gets executed when the class is loaded in the
     * memory.
     */

    static {
        hashMap.put('P', "AA");
        hashMap.put('H', "AD");
        hashMap.put('0', "AF");
        hashMap.put('Q', "AG");
        hashMap.put('G', "AV");
        hashMap.put('6', "AX");
        hashMap.put('4', "DA");
        hashMap.put('M', "DD");
        hashMap.put('E', "DF");
        hashMap.put('A', "DG");
        hashMap.put('1', "DV");
        hashMap.put('Y', "DX");
        hashMap.put('L', "FA");
        hashMap.put('2', "FD");
        hashMap.put('N', "FF");
        hashMap.put('O', "FG");
        hashMap.put('F', "FV");
        hashMap.put('D', "FX");
        hashMap.put('X', "GA");
        hashMap.put('K', "GD");
        hashMap.put('R', "GF");
        hashMap.put('3', "GG");
        hashMap.put('C', "GV");
        hashMap.put('V', "GX");
        hashMap.put('S', "VA");
        hashMap.put('5', "VD");
        hashMap.put('Z', "VF");
        hashMap.put('W', "VG");
        hashMap.put('7', "VV");
        hashMap.put('B', "VX");
        hashMap.put('J', "XA");
        hashMap.put('9', "XD");
        hashMap.put('U', "XF");
        hashMap.put('T', "XG");
        hashMap.put('I', "XV");
        hashMap.put('8', "XX");
        hashMap.put(' ', "__");
        hashMap.put(' ', "..");

        // As the hashMap is filled with unique values, for loop can be used to iterate
        // over the entries of the default hashMap to fill reversedHashMap.

        for (Map.Entry<Character, String> entry : hashMap.entrySet()) {
            reversedHashMap.put(entry.getValue(), entry.getKey());
        }

    }

    public PolybiusSquareCipher(String secretWord) {

        this.secretWord = secretWord.toUpperCase();
    }

    public String encryptWord(String encrypt) {
        String halfEncryption = halfEncryption(encrypt);
        numOfRows = preparingRows(halfEncryption);
        String[][] notSortedMatrix = unSortedGrid(halfEncryption, numOfRows);
        String sortedCipherText = fullEncryption(notSortedMatrix, numOfRows);
        return sortedCipherText;

    }

    public String decryptWord(String sortedCipherText) {
        String[][] sortedMatrix = sortedGrid(sortedCipherText, numOfRows, sortedKey);
        String tempCipherText = halfDecryption(sortedMatrix);
        String originalPlainText = fullDecryption(tempCipherText);
        return originalPlainText;
    }

    /*
     * Method halfEncryption is creating my half encrypted cipher text, the cipher
     * text before columnar transposition. In addition, this method performs a
     * calculation if number of characters in plainText or in key results in
     * creation of irregular grid. If that is the case, the text is appended with
     * extra symbols (dots).
     */
    private String halfEncryption(String input) {
        input = input.toUpperCase();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {

            sb.append(hashMap.get(input.charAt(i)));

        }

        int extraChars = sb.length() % secretWord.length();
        int noOfEmptyBoxes = secretWord.length() - extraChars;

        if (extraChars != 0) {
            for (int i = 0; i < noOfEmptyBoxes; i++) {
                sb.append(".");
            }
        }

        String cipherText = sb.toString();
        System.out.println("\nHalf encrypted cipher text: " + cipherText + "\n\n"); // printing encrypted half encrypted
        // cipher text
        return cipherText;
    }

    /*
     * Method preparingRows calculates, number of rows needed in both, not sorted
     * grid and sorted grid. If length of the cipherText creates regular grid, just
     * one extra row is required to create row for key. Otherwise, 2 additional rows
     * will be added (One for the key and another for remaining characters).
     *
     */
    private int preparingRows(String input) {

        int numOfChars = input.length();
        int numOfRows;

        if (numOfChars % secretWord.length() > 0) {
            numOfRows = numOfChars / secretWord.length();
            numOfRows += 2;
        } else {
            numOfRows = numOfChars / secretWord.length();
            numOfRows += 1;
        }
        return numOfRows;
    }

    /*
     * Method unSortedGrid is filling a grid with characters of the key and
     * cipherText. Process starts with creation of notSortedMatrix in which amount
     * of rows is determined by variable numOfRows, which value was calculated in
     * the previous method. Number of columns is set by the length of variable key.
     */
    private String[][] unSortedGrid(String halfEncryption, int totalRows) {

        int row = 0;
        int column = 0;
        String[][] notSortedMatrix = new String[totalRows][secretWord.length()];
        char eachChar;

        for (int j = 0; j < secretWord.length(); j++) {
            notSortedMatrix[0][j] = Character.toString(secretWord.charAt(j)); // 2 nested for loops are assigning
            // first row of notSortedMatrix to
            // the key.

        }

        row = 1; // Setting the starting position to [1][0] in the notSortedMatrix. (second row,
        // first column)
        column = 0;

        for (int i = 0; i < halfEncryption.length(); i++) {

            eachChar = halfEncryption.charAt(i); // getting each value of text

            notSortedMatrix[row][column] = Character.toString(eachChar); // assigning value of the character to position
            // [1][0]

            ++column; // if the first character is assigned, the program is moving to position [1][1]
            // then [1][2] etc...
            if (column == (secretWord.length())) { // if iteration reaches the last column, column is reseted, and the
                // iteration is moving to position in the next row [2][0]
                column = 0;
                ++row;

            }
            // System.out.println(eachChar); test - working
        }
        System.out.println("\nNot sorted matrix\n");
        System.out.println(
                Arrays.deepToString(notSortedMatrix).replace("], ", "]\n").replace("[[", "[").replace("]]", "]")); // printing
        // not
        // sorted
        // grid
        return notSortedMatrix;
    }

    // Method fullEncryption is reading characters from the notSortedMatrix column
    // by column in alphabetical order.

    private String fullEncryption(String[][] notSortedMatrix, int totalRows) {

        sortedKey = new String[secretWord.length()];

        for (int j = 0; j < secretWord.length(); j++) {
            sortedKey[j] = notSortedMatrix[0][j]; // retrieving key from first row of the notSortedArray and
            // assigning it to String
            // array named sortedKey..

        }

        Arrays.sort(sortedKey); // key in char array is being sorted according UTF-16 code units values.

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < secretWord.length(); i++) {
            for (int column = 0; column < secretWord.length(); column++) {

                if (sortedKey[i] == notSortedMatrix[0][column]) { // if character of the sorted key matches with the
                    // characters of first row in notSortedArray, which
                    // is
                    // not sorted key, StringBuilder sb is appended with
                    // each column below that character

                    for (int row = 1; row < totalRows; row++) {
                        sb.append(notSortedMatrix[row][column]);

                    }
                }

            }
        }
        String sortedCipherText = sb.toString();

        return sortedCipherText;
    }

    /*
     * Method SortedGrid is filling a grid named sortedMatrix. The grid is filled in
     * similar procedure as the grid notSortedMatrix.
     */

    private String[][] sortedGrid(String sortedCipherText, int totalRows, String[] sortedKey) {

        int row = 0;
        int column = 0;
        String[][] sortedMatrix = new String[totalRows][secretWord.length()];

        char eachChar;

        for (int j = 0; j < secretWord.length(); j++) {
            sortedMatrix[0][j] = sortedKey[j];

        }

        row = 1;
        column = 0;

        for (int i = 0; i < sortedCipherText.length(); i++) {

            eachChar = sortedCipherText.charAt(i);

            sortedMatrix[row][column] = Character.toString(eachChar);

            ++row;
            if (row == totalRows) {
                row = 1;
                ++column;

            }

        }

        System.out.println("\nSorted matrix\n");
        System.out
                .println(Arrays.deepToString(sortedMatrix).replace("], ", "]\n").replace("[[", "[").replace("]]", "]"));
        return sortedMatrix;
    }

    /*
     * Method halfDecryption is returning half decrypted text, which can be later
     * decrypted to plainText. It is being performed with a help of 2D array named
     * decryptMatrix.
     *
     */

    private String halfDecryption(String[][] sortedMatrix) {

        String[] keyInArray = new String[secretWord.length()];
        String halfDecryptedText;
        String tempCipherText;
        String[][] decryptMatrix = new String[numOfRows][secretWord.length()];

        for (int j = 0; j < secretWord.length(); j++) {
            keyInArray[j] = Character.toString(secretWord.charAt(j)); // assigning chars of key to String array.
        }

        /*
         * Following iterations are reading characters from sortedMatrix column by
         * column. This process requires to read columns in correct order, which is set
         * by default sequence of characters in the variable key.
         */

        Set<Integer> alreadyUsedIndexes = new HashSet<>();
        StringBuilder sb = new StringBuilder();
        outerloop: for (int i = 0; i < secretWord.length(); i++) { // outerloop is iterating over characters of the key.
            for (int j = 0; j < secretWord.length(); j++) { // innerloop is iterating over values of the first row in
                // sortedMatrix, which is sorted key..

                if (keyInArray[i].equals(sortedMatrix[0][j])) { // if character of the sorted key matches with the
                    // characters of first row in sortedArray, which is
                    // key sorted in alphabetical order, StringBuilder sb is
                    // appended with
                    // each column below that character

                    if (alreadyUsedIndexes.add(j)) { // keeping evidence of already appended columns. IF the key
                        // contains
                        // duplicates, program needs to append just one column and not
                        // the one which has been already appended.
                        for (int row = 1; row < numOfRows; row++) {
                            sb.append(sortedMatrix[row][j]);

                        }
                        continue outerloop; // If the program finds first match, iteration continues to avoid getting
                        // the
                        // second match, in case the key contains duplicate letters.
                    }

                }
            }
        }
        halfDecryptedText = sb.toString();
        System.out.println("\nHalf decrypted text: " + halfDecryptedText + "\n\n");

        int row = 0;
        int column = 0;

        char eachChar;

        for (int j = 0; j < secretWord.length(); j++) { // creating helping 2D array to perform the decryption
            // process. Filling process uses similar approach from
            // constructing notSorted
            // and sortedMatrix.
            decryptMatrix[0][j] = keyInArray[j];

        }

        row = 1;
        column = 0;

        for (int i = 0; i < halfDecryptedText.length(); i++) {

            eachChar = halfDecryptedText.charAt(i);

            decryptMatrix[row][column] = Character.toString(eachChar);

            ++row;
            if (row == numOfRows) {
                row = 1;
                ++column;

            }

        }

        System.out.println("Default not sorted matrix\n");
        System.out.println(
                Arrays.deepToString(decryptMatrix).replace("], ", "]\n").replace("[[", "[").replace("]]", "]"));

        // retrieving tempCipherText from decryptMatrix.

        StringBuilder sb2 = new StringBuilder();

        for (int i = 1; i < numOfRows; i++) {
            for (int j = 0; j < secretWord.length(); j++) {
                sb2.append(decryptMatrix[i][j]); // index row is starting at 1, to avoid first row
            }
        }
        tempCipherText = sb2.toString();
        System.out.println("\nHalf decryption: " + tempCipherText);
        return tempCipherText;
    }

    /*
     * Method fullDecryption is iterating over tempCipherText. In order to get valid
     * character for hashmap, program needs to get every 2 characters, therefore
     * variables counter and tempValues is initialised.
     *
     */

    private String fullDecryption(String tempCipherText) {

        StringBuilder sb = new StringBuilder();
        String originalPlainText;
        int counter = 0;
        String tempValues = "";

        for (int i = 0; i < tempCipherText.length(); i++) {
            counter++;
            tempValues += Character.toString(tempCipherText.charAt(i)); // getting characters from tempCipherText.

            if (counter == 2) { // if program gets 2 characters...
                sb.append(reversedHashMap.get(tempValues)); // ...they are being decrypted with a help of the second
                // hashmap.
                counter = 0;
                tempValues = ""; // variables reseted to default.
            }

        }

        originalPlainText = sb.toString();
        return originalPlainText;
    }
}
