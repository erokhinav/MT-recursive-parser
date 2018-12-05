import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;

public class ParserTest {
    private Parser parser;
    private String string;

    @Before
    public void setUp() {
        parser = new Parser();
    }

    @Test
    public void eps() {
        string = "";
        check();
    }

    @Test
    public void letter() {
        string = "a";
        check();
    }

    @Test
    public void choice() {
        string = "a|b";
        check();
    }

    @Test
    public void concat() {
        string = "ab";
        check();
    }

    @Test
    public void brackets_empty() {
        string = "()";
        check();
    }

    @Test
    public void brackets() {
        string = "(a|bc)";
        check();
    }

    @Test
    public void kleene() {
        string = "a*";
        check();
    }

    @Test
    public void plus() {
        string = "a+";
        check();
    }

    @Test
    public void wrong() {
        InputStream stream = new ByteArrayInputStream("a*+".getBytes(StandardCharsets.UTF_8));
        try {
            parser.parse(stream);
            System.out.println("Error");
        } catch (AssertionError e) {
            System.out.println(e.getMessage());
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void random() {
        string = "((abc*b|a)*ab(aa|b*)b)*";
        check();
    }

    private void check() {
        InputStream stream = new ByteArrayInputStream(string.getBytes(StandardCharsets.UTF_8));
        try {
            Tree tree = parser.parse(stream);
            System.out.println(tree);
        } catch (ParseException e) {
            System.out.println();
        }
    }
}