import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
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
        LinkedList<String> twords = new LinkedList<>();
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
        if (word.equals("")) {
            return;
        }
        char start = (char) (word.charAt(0) - 'a');
        if (node.edgeLabel[start] == null) {
            //New Word, New Node
            node.edgeLabel[start] = word;
            node.children[start] = new TrieNode();
            node.children[start].ender = true;
        } else if (node.edgeLabel[start].equals(word)){
            //Word is the exact same as this edge
            node.children[start].ender = true;
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
            node.children[start].ender = true;

            tempS = tempS.substring(word.length(), tempS.length());
            node.children[start].edgeLabel[(char) tempS.charAt(0) - 'a'] = tempS;
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

            //Bug Fixing, idk where it is a problem
            node.children[start].ender = false;
            node.children[start].children[(char) tempS.charAt(0) - 'a'].ender = true;
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
        LinkedList<TrieNode> q = new LinkedList<>();
        LinkedList<String> sQ = new LinkedList<>();
        LinkedList<Integer> iQ = new LinkedList<>();
        TrieNode temp = root;
        String tempS = arg;
        int depth = 0;

        //Find node of best copy
        while (true) {
            depth++;
            int start = tempS.charAt(0) - 'a';
            if(temp.children[start] == null) {
                break;
            } else if (temp.edgeLabel[start].length() < tempS.length()) {
                //If arg is longer than the edge it should be at, check if it fits
                if (temp.edgeLabel[start].equals(tempS.substring(0,temp.edgeLabel[start].length()))) {
                    //If yes, dive deeper
                    tempS = tempS.substring(temp.edgeLabel[start].length());
                    temp = temp.children[start];
                    continue;
                } else {
                    //If it doesn't, break it off
                    break;
                }
            } else if (temp.edgeLabel[start].equals(tempS)) {
                temp = temp.children[start];
                depth++;
                break;
            } else {
                break;
            }
        }


        //Print to output
        try {
            File out = new File(outputfile);
            FileWriter writer = new FileWriter(out);

            q.add(temp);
            sQ.add(arg);
            iQ.add(depth);
            while (!q.isEmpty()) {
                temp = q.remove();
                tempS = sQ.remove();
                depth = iQ.remove();

                for (int i = 0; i < 26; i++) {
                    if (temp.children[i] != null) {
                        q.add(temp.children[i]);
                        sQ.add(tempS + temp.edgeLabel[i]);
                        iQ.add(depth + 1);
                        if (temp.children[i].ender) {
                            writer.write(tempS + temp.edgeLabel[i] + " " + depth + "\n");
                        }
                    }
                }
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
