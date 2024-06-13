package Chat;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

class Huffman {
    private static final int MAX_CHARACTERS = 256;
    private static Node root; // Static member for the root of the Huffman tree


    private static class Node implements Comparable<Node> {
        char character;
        int frequency;
        Node left;
        Node right;

        Node(char character, int frequency) {
            this.character = character;
            this.frequency = frequency;
        }

        Node(int frequency, Node left, Node right) {
            this.frequency = frequency;
            this.left = left;
            this.right = right;
        }

        boolean isLeaf() {
            return left == null && right == null;
        }

        public int compareTo(Node node) {
            return this.frequency - node.frequency;
        }
    }

    public static Map<Character, String> compress(String message) {
        int[] frequencies = getFrequencies(message);
        Node root = buildHuffmanTree(frequencies);
        Map<Character, String> charToCode = new HashMap<>();
        buildCodeTable(root, "", charToCode);
        return charToCode;
    }

    private static int[] getFrequencies(String message) {
        int[] frequencies = new int[MAX_CHARACTERS];
        for (char c : message.toCharArray()) {
            frequencies[c]++;
        }
        return frequencies;
    }

    private static Node buildHuffmanTree(int[] frequencies) {
        PriorityQueue<Node> pq = new PriorityQueue<>();
        for (char c = 0; c < MAX_CHARACTERS; c++) {
            if (frequencies[c] > 0) {
                pq.add(new Node(c, frequencies[c]));
            }
        }
        while (pq.size() > 1) {
            Node left = pq.poll();
            Node right = pq.poll();
            Node parent = new Node(left.frequency + right.frequency, left, right);
            pq.add(parent);
        }
        return pq.poll();
    }

    private static void buildCodeTable(Node node, String code, Map<Character, String> charToCode) {
        if (node.isLeaf()) {
            charToCode.put(node.character, code);
        } else {
            buildCodeTable(node.left, code + "0", charToCode);
            buildCodeTable(node.right, code + "1", charToCode);
        }
    }

    public static Map<String, Character> getDecodingTable() {
        Map<String, Character> codeToChar = new HashMap<>();
        buildDecodingTable(codeToChar, "", root);
        return codeToChar;
    }

    private static void buildDecodingTable(Map<String, Character> codeToChar, String code, Node node) {
        if (node.isLeaf()) {
            codeToChar.put(code, node.character);
        } else {
            buildDecodingTable(codeToChar, code + "0", node.left);
            buildDecodingTable(codeToChar, code + "1", node.right);
        }
    }
}
