import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class Trie {
    String arg = "";
    TrieNode root = new TrieNode();

    /**
     *
     * This function is used to build tree.
     * Inputfile should be read in this function in order to use testcases.
     *
     * @param inputfile
     * @throws Exception
     */
    public void buildTrie (String inputfile) throws Exception {
        ArrayList<String> twords = new ArrayList<>();
        //Read the file for argument and trie words
        try {
            File file = new File(inputfile);
            Scanner read = new Scanner(file);
            //Find argument
            if (read.hasNextLine()) {
                arg = read.nextLine();
            } else {
                throw new Exception("File Empty!");
            }
            //Add trie words
            while (read.hasNextLine()) {
                twords.add(read.nextLine());
            }

            read.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        //Build the trie
        for (String word : twords) {
            insert(word, root);
        }
    }

    private void insert(String word, TrieNode node) {
        char start = (char) (word.charAt(0) - 'a');
        if (node.edgeLabel[start] == null) {
            //New Word, New Node
            node.edgeLabel[start] = word;
            node.children[start] = new TrieNode();
            node.children[start].ender = true;
        } else if (node.edgeLabel[start].equals(word)){
            //Word is the exact same as this edge
            node.ender = true;
        } else if (word.length() > node.edgeLabel[start].length() && word.substring(0, node.edgeLabel[start].length()).equals(node.edgeLabel[start])) {
            //Word has a suffix of node
            //Recursively call insert, with the non-matching portion of word
            insert(word.substring(node.edgeLabel[start].length()), node.children[start]);
        } else if (word.length() < node.edgeLabel[start].length() && node.edgeLabel[start].substring(0, word.length()).equals(word)) {
            //Word is a prefix of node
            TrieNode temp = node.children[start];
            String tempS = node.edgeLabel[start];

            node.children[start] = new TrieNode();
            node.edgeLabel[start] = word;

            tempS = tempS.substring(word.length(), tempS.length());
            temp.edgeLabel[(char) tempS.charAt(0) - 'a'] = tempS;

            node.children[start].children[(char) tempS.charAt(0) - 'a'] = temp;
        } else {
            //Partial Match
            int index = 0;
            //Get index of incorrect match
            for(;  index < word.length() && index < node.edgeLabel[start].length(); index++) {
                if (word.charAt(index) != node.edgeLabel[start].charAt(index)) {
                    break;
                }
            }

            //Split into two nodes
            TrieNode temp = node.children[start];
            String tempS = node.edgeLabel[start];
            String shortW = word.substring(index);

            //New origin node
            node.children[start] = new TrieNode();
            node.edgeLabel[(char) tempS.charAt(0) - 'a'] = tempS.substring(0, index);

            //Shorten tempS
            tempS = tempS.substring(index);

            //Children are the end of the new word and the end of the original word
            node.children[start].edgeLabel[(char) shortW.charAt(0) - 'a'] = shortW;
            node.children[start].edgeLabel[(char) tempS.charAt(0) - 'a'] = tempS;
            node.children[start].children[(char) tempS.charAt(0) - 'a'] = temp;
            node.children[start].children[(char) shortW.charAt(0) - 'a'] = new TrieNode();
            node.children[start].children[(char) shortW.charAt(0) - 'a'].ender = true;
        }
    }

    /**
     *
     * This function is used to traverse the trie and compute the autocomplete wordlist.
     * Output file should be produced here.
     *
     * @param outputfile
     * @throws Exception
     */
    public void autocomplete (String outputfile) throws Exception{
        ArrayList<String> oStrings = new ArrayList<>();
        ArrayList<Integer> oWeights = new ArrayList<>();
        TrieNode temp = root;

        while (arg != "") {
            if (temp.children[(char) arg.charAt(0) - 'a'] != null) {
                for (int i = 0; i <= temp.edgeLabel[(char) arg.charAt(0) - 'a'].length(); i++) {

                }

            } else {
                break;
            }
        }

        printList(temp, oStrings, oWeights);


    }

    private void printList(TrieNode root, ArrayList<String> oStrings, ArrayList<Integer> oWeights) {

    }
}
