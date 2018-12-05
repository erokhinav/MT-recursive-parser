import javax.swing.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;

public class Main {
    /**
     * An alternative starting point for this demo, to also allow running this applet as an
     * application.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) throws ParseException {
        Graph applet = new Graph();
        Tree tree = parse("a|(bc*)");
        applet.addTree(tree);
        applet.init();

        JFrame frame = new JFrame();
        frame.getContentPane().add(applet);
        frame.setTitle("JGraphT Adapter to JGraphX Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private static Tree parse(String string) throws ParseException {
        InputStream stream = new ByteArrayInputStream(string.getBytes(StandardCharsets.UTF_8));
        Parser parser = new Parser();
        return parser.parse(stream);
    }
}
