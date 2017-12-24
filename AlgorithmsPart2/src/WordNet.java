import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordNet {
    private static final String COMMA = ",";
    private static final String SPACE = " ";

    private Map<Integer, List<String>> idxToNouns = new HashMap<>();
    private Map<String, List<Integer>> nounToIdx = new HashMap<>();
    private SAP sap;

    /**
     * constructor takes the name of the two input files
     */
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException("Synsets or hypernyms are null");
        }

        parseSynsetsFile(synsets);
        Digraph digraph = parseHypernymsFile(hypernyms);
        sap = new SAP(digraph);
    }

    private void parseSynsetsFile(String synsets) {
        In synsetsIn = new In(synsets);
        for (String line = synsetsIn.readLine(); line != null; line = synsetsIn.readLine()) {
            String[] split = line.split(COMMA);
            Integer index = Integer.valueOf(split[0]);
            List<String> nouns = Arrays.asList(split[1].split(SPACE));
            idxToNouns.put(index, nouns);

            List<Integer> indexes;
            for (String noun : nouns) {
                indexes = nounToIdx.get(noun);
                if (indexes == null) {
                    indexes = new ArrayList<>();
                }
                indexes.add(index);
                nounToIdx.put(noun, indexes);
            }
        }
        synsetsIn.close();
    }

    private Digraph parseHypernymsFile(String hypernyms) {
        Digraph graph = new Digraph(idxToNouns.size());
        In hypernymsIn = new In(hypernyms);
        String line = hypernymsIn.readLine();

        while (line != null) {
            String[] split = line.split(COMMA);
            int v = Integer.parseInt(split[0]);
            for (int i = 1; i < split.length; i++) {
                graph.addEdge(v, Integer.parseInt(split[i]));
            }

            line = hypernymsIn.readLine();
        }
        hypernymsIn.close();

        if (!isDAG(graph)) {
            throw new IllegalArgumentException("The digraph is not DAG!");
        }
        return graph;
    }

    private boolean isDAG(Digraph digraph) {
        DirectedCycle directedCycle = new DirectedCycle(digraph);
        return !directedCycle.hasCycle();
    }

    /**
     * returns all WordNet nouns
     */
    public Iterable<String> nouns() {
        return nounToIdx.keySet();
    }

    /**
     * is the word a WordNet noun?
     */
    public boolean isNoun(String word) {
        if (word == null) {
            throw new IllegalArgumentException("Input argument is null");
        }

        return nounToIdx.containsKey(word);
    }

    /**
     * distance between nounA and nounB (defined below)
     */
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null) {
            throw new IllegalArgumentException("Some input argument is null");
        }

        return Integer.MIN_VALUE;
    }

    /**
     * a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
     * in a shortest ancestral path (defined below)
     */
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null) {
            throw new IllegalArgumentException("Some input argument is null");
        }

        return null;
    }

    /**
     * do unit testing of this class
     */
    public static void main(String[] args) {
        WordNet wordNet = new WordNet("AlgorithmsPart2\\synsets.txt", "AlgorithmsPart2\\hypernyms.txt");
    }
}
