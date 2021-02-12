package ie.gmit.dip;

import java.util.Scanner;

public class Menu {

    public static final String ENCRYPTED_TEXT = "Encrypted text:";
    public static final String DECRYPTED_TEXT = "Original plain text:";

    public void start() {
        Scanner in = new Scanner(System.in);
        while (true) {

            // Console menu output.

            System.out.println("********************************");
            System.out.println("_____Polybius Square Cypher_____");
            System.out.println("********************************");
            System.out.println("1, Encryption");
            System.out.println("2, Encryption and decryption");
            System.out.println("3, Exit");
            System.out.println("Select an option(1-3)");

            // parsing input
            int selection = Integer.parseInt(in.nextLine());
            ;

            // running methods according the selection.
            if (selection == 1) {
                encryptionProcess(in);
            } else if (selection == 2) {
                encAndDecProcess(in);
            } else if (selection == 3) {
                break;
            } else {
                System.out.println("Invalid choice");
            }
        }
        in.close();
    }

    // Methods encryptionProcess, decryptionProcess and encAndDecProcess ask user
    // for an input, which can be either plain text or encrypted text and key. Logic
    // of encrypting and decrypting algorithms is described in the
    // PolybiusSquareCipher class

    private void encryptionProcess(Scanner in) {
        System.out.println("Enter text for encryption");
        String plainText = in.nextLine();
        System.out.println("Enter the key for encryption");
        String key = in.nextLine();
        System.out.println("********************************");
        PolybiusSquareCipher polybius = new PolybiusSquareCipher(key);
        String encrypted = polybius.encryptWord(plainText);
        System.out.println(ENCRYPTED_TEXT + " " + encrypted);
    }


    private void encAndDecProcess(Scanner in) {
        System.out.println("Enter text for encrypting and decrypting process");
        String plainText = in.nextLine();
        System.out.println("Enter the key for encrypting and decrypting process");
        String key = in.nextLine();
        System.out.println("********************************");
        PolybiusSquareCipher polybius = new PolybiusSquareCipher(key);
        String encrypted = polybius.encryptWord(plainText);
        System.out.println(ENCRYPTED_TEXT + " " + encrypted);
        String decrypted = polybius.decryptWord(encrypted);
        System.out.println(DECRYPTED_TEXT + " " + decrypted);
    }

}
