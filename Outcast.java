/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private final WordNet wn;

    public Outcast(WordNet wordnet) {
        wn = wordnet;
    }

    public String outcast(String[] nouns) {
        String r = "";
        int max = 0;
        for (String nounI : nouns) {
            // StdOut.println(nounI);
            int sum = 0;
            for (String nounO : nouns) {
                sum += wn.distance(nounI, nounO);
                // StdOut.println(nounO);
                // StdOut.println(wn.distance(nounI, nounO));
            }
            // StdOut.println(sum);
            if (sum > max) {
                max = sum;
                r = nounI;
            }
        }
        return r;
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
