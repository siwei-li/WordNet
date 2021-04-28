/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class SAP {
    private final Digraph graph;

    private void checkNull(Object... objects) {
        for (Object o : objects) {
            if (o == null) {
                IllegalArgumentException e = new IllegalArgumentException();
                throw e;
            }
        }
    }

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        checkNull(G);
        // !!important: if you want to make the graph immutable, try to make a deep copy!
        graph = new Digraph(G);

    }

    private int[] compute(int v, int w) {

        boolean found = false;
        int min = Integer.MAX_VALUE;
        int[] r = { -1, -1 };

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(graph, w);
        for (int i = 0; i < graph.V(); i++) {
            if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
                found = true;
                if (bfsV.distTo(i) + bfsW.distTo(i) < min) {
                    min = bfsV.distTo(i) + bfsW.distTo(i);
                    r[1] = i;
                }
            }
        }

        if (found) r[0] = min;
        return r;
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        int[] r = compute(v, w);
        return r[0];
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        int[] r = compute(v, w);
        return r[1];
    }


    private int[] compute(Iterable<Integer> v, Iterable<Integer> w) {
        checkNull(v, w);

        boolean found = false;
        int min = Integer.MAX_VALUE;
        int[] r = { -1, -1 };

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(graph, w);
        for (int i = 0; i < graph.V(); i++) {
            if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
                found = true;
                if (bfsV.distTo(i) + bfsW.distTo(i) < min) {
                    min = bfsV.distTo(i) + bfsW.distTo(i);
                    r[1] = i;
                }
            }
        }

        if (found) r[0] = min;
        return r;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        int[] r = compute(v, w);
        return r[0];
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        int[] r = compute(v, w);
        return r[1];
    }


    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        // StdOut.println(sap.length(7,10));
        // StdOut.println(sap.ancestor(7,10));
        Integer a[] = { 13, 23, 24 };
        Integer b[] = { 6, 16, 17 };
        Iterable<Integer> A = Arrays.asList(a);
        Iterable<Integer> B = Arrays.asList(b);
        StdOut.println(sap.length(A, B));
        StdOut.println(sap.ancestor(A, B));
    }
}
