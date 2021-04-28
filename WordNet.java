/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;

public class WordNet {
    private boolean[] dagMarked;
    private boolean[] dagFinished;

    // private int synCnt;
    private final ST<Integer,String> syn;
    private final HashMap<String,SET<Integer>> nounSet;
    private final SAP sap;

    // check null input
    private void checkNull(String... s) {
        for (String i : s) {
            if (i == null) {
                IllegalArgumentException e = new IllegalArgumentException();
                throw e;
            }
        }
    }

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        checkNull(synsets, hypernyms);
        In synset = new In(synsets);
        In hyperset = new In(hypernyms);

        syn = new ST<Integer, String>();
        // synCnt = 0;
        nounSet = new HashMap<String, SET<Integer>>();
        while (!synset.isEmpty()) {
            String line = synset.readLine();
            // synCnt++;
            String[] words = line.split(",");
            syn.put(Integer.parseInt(words[0]), words[1]);

            String[] nouns = words[1].split(" ");
            for (String noun : nouns) {
                SET<Integer> s;
                if (!nounSet.containsKey(noun)) {
                    s = new SET<Integer>();
                }
                else {
                    s = nounSet.get(noun);
                }
                s.add(Integer.parseInt(words[0]));
                nounSet.put(noun, s);
            }
        }
        // for (Object i : syn.keys())
        //     StdOut.println(i + " " + syn.get((Integer) i));


        int V = 0;
        // int E = 0;
        Bag<int[]> bag = new Bag<int[]>();
        while (!hyperset.isEmpty()) {
            String line = hyperset.readLine();

            String[] words = line.split(",");
            // E += words.length - 1;
            int n0 = Integer.parseInt(words[0]);
            if (n0 + 1 > V) V = n0 + 1;

            for (int i = 1; i < words.length; i++) {
                int num = Integer.parseInt(words[i]);
                if (num + 1 > V) V = num + 1;
                bag.add(new int[] { n0, num });
            }

        }

        Digraph G = new Digraph(V);
        for (Object pair : bag) {
            int[] p = (int[]) pair;
            G.addEdge(p[0], p[1]);
        }
        // StdOut.println(G);
        int roots = 0;
        dagMarked = new boolean[G.V()];
        dagFinished = new boolean[G.V()];
        for (int v = 0; v < G.V(); v++) {
            if (!dagMarked[v]) rDAG(v, G);
            if (G.outdegree(v)==0) roots+=1;
        }

        if (roots!=1) throw new IllegalArgumentException();
        sap = new SAP(G);
    }

    private void rDAG(int v, Digraph graph) {
        dagMarked[v] = true;
        for (int w : graph.adj(v)) {
            if (!dagMarked[w])
                rDAG(w, graph);
            else {
                if (!dagFinished[w]) {
                    IllegalArgumentException e = new IllegalArgumentException();
                    throw e;
                }
            }
        }
        dagFinished[v] = true;
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nounSet.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        checkNull(word);
        return nounSet.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        checkNull(nounA, nounB);
        if (!isNoun(nounA) || !isNoun(nounB)) {
            IllegalArgumentException e = new IllegalArgumentException();
            throw e;
        }
        SET<Integer> a = nounSet.get(nounA);
        SET<Integer> b = nounSet.get(nounB);

        return sap.length(a, b);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        checkNull(nounA, nounB);
        if (!isNoun(nounA) || !isNoun(nounB)) {
            IllegalArgumentException e = new IllegalArgumentException();
            throw e;
        }

        SET<Integer> a = nounSet.get(nounA);
        SET<Integer> b = nounSet.get(nounB);
        int i = sap.ancestor(a, b);

        return syn.get(i);
    }

    public static void main(String[] args) {

        WordNet wn = new WordNet(args[0], args[1]);
        // StdOut.println(wn.nouns());
        StdOut.println(wn.distance("bed", "coffee"));
        StdOut.println(wn.distance("bed", "water"));
        StdOut.println(wn.distance("water", "coffee"));

    }
}
