import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxCellRenderer;
import org.jgrapht.*;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;

/**
 * A demo applet that shows how to use JGraphX to visualize JGraphT graphs. Applet based on
 * JGraphAdapterDemo.
 *
 */
public class Graph
        extends
        JApplet
{
    private static final long serialVersionUID = 2202072534703043194L;

    private static final Dimension DEFAULT_SIZE = new Dimension(530, 320);

    private JGraphXAdapter<String, DefaultEdge> jgxAdapter;

    private Tree tree;

//    class Edge extends DefaultEdge {
//        @Override
//        public String toString() {
//            return "";
//        }
//    }

//    /**
//     * An alternative starting point for this demo, to also allow running this applet as an
//     * application.
//     *
//     * @param args command line arguments
//     */
//    public static void main(String[] args) throws ParseException {
//        Graph applet = new Graph();
//        applet.init();
//
//        JFrame frame = new JFrame();
//        frame.getContentPane().add(applet);
//        frame.setTitle("JGraphT Adapter to JGraphX Demo");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.pack();
//        frame.setVisible(true);
//    }

    public void addTree(Tree tree) {
        this.tree = tree;
    }

    void buildGraph(ListenableGraph<String, DefaultEdge> g, Tree tree) {
        if (tree.children != null) {
           for (Tree child : tree.children) {
               g.addVertex(child.node);
               g.addEdge(tree.node, child.node);
               buildGraph(g, child);
           }
        }
    }

    @Override
    public void init()
    {
        // create a JGraphT graph
        ListenableGraph<String, DefaultEdge> g =
                new DefaultListenableGraph<>(new DefaultDirectedGraph<>(DefaultEdge.class));

        // create a visualization using JGraph, via an adapter
        jgxAdapter = new JGraphXAdapter<>(g);

        setPreferredSize(DEFAULT_SIZE);
        mxGraphComponent component = new mxGraphComponent(jgxAdapter);
        component.setConnectable(false);
        component.getGraph().setAllowDanglingEdges(false);
        getContentPane().add(component);
        resize(DEFAULT_SIZE);

        g.addVertex(tree.node);
        buildGraph(g, tree);

//        String v1 = "v111111";
//        String v2 = "v2";
//        String v3 = "v3";
//        String v4 = "v4";
//
//
//        g.addVertex(v1);
//        g.addVertex(v2);
//        g.addVertex(v3);
//        g.addVertex(v4);
//
//        g.addEdge(v1, v3);

        // positioning via jgraphx layouts
        mxCircleLayout layout = new mxCircleLayout(jgxAdapter);

        // center the circle
//        int radius = 100;
//        layout.setX0((DEFAULT_SIZE.width / 2.0) - radius);
//        layout.setY0((DEFAULT_SIZE.height / 2.0) - radius);
//        layout.setRadius(radius);
//        layout.setMoveCircle(true);

        layout.execute(jgxAdapter.getDefaultParent());
        // that's all there is to it!...

        BufferedImage image =
                mxCellRenderer.createBufferedImage(jgxAdapter, null, 2, Color.WHITE, true, null);
        File imgFile = new File("src/graph.png");
        try {
            ImageIO.write(image, "PNG", imgFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}