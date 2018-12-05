import guru.nidi.graphviz.attribute.*;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.*;
import org.jgrapht.ListenableGraph;
import org.jgrapht.graph.DefaultEdge;
import static guru.nidi.graphviz.model.Factory.*;

import javax.swing.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;

public class Main {
    private int count = 0;
    /**
     * An alternative starting point for this demo, to also allow running this applet as an
     * application.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) throws ParseException, IOException {
        Tree tree = parse("abc");

//        MutableGraph graph = mutGraph("tree").setDirected(true).add(node("a").link(node("b").with(Label.of("a"))));
        MutableGraph graph = mutGraph("tree").setDirected(true);
//        Node nodeB = node("b");
//        Node nodeC = node("c");
//        graph.add(nodeA.link(nodeC));
//        graph.add(nodeB.link(nodeC));
        buildGraph(graph, tree);
        Graphviz.fromGraph(graph).width(800).render(Format.PNG).toFile(new File("src/graph.png"));
    }

    private static Integer globalVerId = 0;

    private static Tree parse(String string) throws ParseException {
        InputStream stream = new ByteArrayInputStream(string.getBytes(StandardCharsets.UTF_8));
        Parser parser = new Parser();
        return parser.parse(stream);
    }

    private static MutableNode buildGraph(MutableGraph graph, Tree tree) {
        MutableNode curNode = mutNode(tree.node + " [" + (globalVerId++) + "]");
        if (tree.children != null) {
            for (Tree child : tree.children) {
                MutableNode otherNode = buildGraph(graph, child);
                graph.add(curNode.addLink(otherNode));
            }
        } else {
            graph.add(curNode);
        }
        return curNode;
    }
}
