package Chat;

import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;
import javax.swing.JTextField;

public class UserOne implements Runnable {
    JTextField text;
    static DataOutputStream dout;

    BufferedReader reader;
    BufferedWriter writer;
    String name = "Abid";

    UserOne() {
        try {
            Socket socket = new Socket("localhost", 2003);
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed() {
        try {
            String message = text.getText();
            String compressedMessage = compress(message);

            try {
                writer.write(message);
                writer.write("\r\n");
                writer.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }

            text.setText("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String compress(String message) {
        Map<Character, String> charToCode = Huffman.compress(message);
        StringBuilder compressed = new StringBuilder();
        for (char c : message.toCharArray()) {
            compressed.append(charToCode.get(c));
        }
        return compressed.toString();
    }

    private String decompress(String compressedMessage) {
        Map<String, Character> codeToChar = Huffman.getDecodingTable();
        StringBuilder decompressed = new StringBuilder();
        StringBuilder currentCode = new StringBuilder();
        for (char c : compressedMessage.toCharArray()) {
            currentCode.append(c);
            if (codeToChar.containsKey(currentCode.toString())) {
                char character = codeToChar.get(currentCode.toString());
                decompressed.append(character);
                currentCode = new StringBuilder();
            }
        }
        return decompressed.toString();
    }

    public void run() {
        try {
            String msg = "";
            while (true) {
                msg = reader.readLine();
                if (msg.contains(name)) {
                    continue;
                }

                System.out.println(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        UserOne two = new UserOne();
        Thread t1 = new Thread(two);
        t1.start();
    }
}
