import java.util.LinkedList;
import java.util.List;

public class TrieNode {
    TrieNode[] children = new TrieNode[26];
    String[] edgeLabel = new String[26];
    boolean ender = false;
}
