import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
    private static final int NO_SUCH = -1;
    private Digraph digraph;

    /**
     * constructor takes a digraph (not necessarily a DAG)
     */
    public SAP(Digraph G) {
        if (G == null) {
            throw new IllegalArgumentException("Digraph is null");
        }
        digraph = G;
    }

    /**
     * length of shortest ancestral path between v and w; -1 if no such path
     */
    public int length(int v, int w) {
        checkVertices(v, w);

        return findAncestor(v, w).getDistance();
    }

    /**
     * a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
     */
    public int ancestor(int v, int w) {
        checkVertices(v, w);

        return findAncestor(v, w).getVertex();
    }

    /**
     * length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
     */
    public int length(Iterable<Integer> verticesV, Iterable<Integer> verticesW) {
        checkVertices(verticesV, verticesW);

        return findAncestor(verticesV, verticesW).getDistance();
    }

    /**
     * a common ancestor that participates in shortest ancestral path; -1 if no such path
     */
    public int ancestor(Iterable<Integer> verticesV, Iterable<Integer> verticesW) {
        checkVertices(verticesV, verticesW);

        return findAncestor(verticesV, verticesW).getVertex();
    }

    private AncestorPair findAncestor(Iterable<Integer> verticesV, Iterable<Integer> verticesW) {
        AncestorPair optimalAncestor = new AncestorPair(NO_SUCH, NO_SUCH);
        for (Integer v : verticesV) {
            for (Integer w : verticesW) {
                AncestorPair currentPair = findAncestor(v, w);
                if (optimalAncestor.getDistance() == NO_SUCH
                        || optimalAncestor.getDistance() > currentPair.getDistance()) {
                    optimalAncestor = currentPair;
                }
            }
        }
        return optimalAncestor;
    }

    private AncestorPair findAncestor(int v, int w) {
        if (v == w) {
            return new AncestorPair(v, 0);
        }
        AncestorPair ancestorPair = new AncestorPair(NO_SUCH, NO_SUCH);
        BreadthFirstDirectedPaths bfdpFromV = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths bfdpFromW = new BreadthFirstDirectedPaths(digraph, w);

        for (int i = 0; i < digraph.V(); i++) {
            if (bfdpFromV.hasPathTo(i) && bfdpFromW.hasPathTo(i)) {
                int currentDist = bfdpFromV.distTo(i) + bfdpFromW.distTo(i);
                if (ancestorPair.getDistance() < 0
                        || ancestorPair.getDistance() > currentDist) {
                    ancestorPair.setDistance(currentDist);
                    ancestorPair.setVertex(i);
                }
            }
        }
        return ancestorPair;
    }

    private void checkVertices(Iterable<Integer> verticesV, Iterable<Integer> verticesW) {
        for (Integer v : verticesV) {
            checkVertex(v);
        }
        for (Integer w : verticesW) {
            checkVertex(w);
        }
    }

    private void checkVertices(int v, int w) {
        checkVertex(v);
        checkVertex(w);
    }

    private void checkVertex(int v) {
        if (v < 0 || v > digraph.V()) {
            throw new IllegalArgumentException("The vertex " + v + " is out of bound " + 0 + " " + digraph.V());
        }
    }

    private class AncestorPair {
        private int vertex;
        private int distance;

        AncestorPair(int vertex, int distance) {
            this.vertex = vertex;
            this.distance = distance;
        }

        int getVertex() {
            return vertex;
        }

        void setVertex(int vertex) {
            this.vertex = vertex;
        }

        int getDistance() {
            return distance;
        }

        void setDistance(int distance) {
            this.distance = distance;
        }
    }

    /**
     * do unit testing of this class
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
