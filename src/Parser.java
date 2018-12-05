import java.io.InputStream;
import java.text.ParseException;

public class Parser {
    LexicalAnalyser lex;
    int counter = 0;

    private Tree Reg() throws ParseException {
        switch (lex.curToken()) {
            case OPEN:
            case LETTER:
                // Choice
                Tree choice = Choice();
                // Reg'
                Tree regPrime = RegPrime();
                return new Tree(format("Reg"), choice, regPrime);
            case BAR:
                // Reg'
                Tree regPrime1 = RegPrime();
                return new Tree(format("Reg"), regPrime1);
            case CLOSE:
            case END:
                // eps
                return new Tree(format("Reg"));
            default:
                throw new AssertionError();
        }
    }

    private Tree Choice() throws ParseException {
        switch (lex.curToken()) {
            case OPEN:
            case LETTER:
                // Part
                Tree part = Part();
                // Choice'
                Tree choicePrime = ChoicePrime();
                return new Tree(format("Choice"), part, choicePrime);
            case CLOSE:
            case BAR:
            case END:
                // eps
                return new Tree(format("Choice"));
            default:
                throw new AssertionError();
        }
    }

    private Tree Part() throws ParseException {
        switch (lex.curToken()) {
            case OPEN:
            case LETTER:
                // Entity
                Tree entity = Entity();
                // Kleene
                Tree kleene = Kleene();
                return new Tree(format("Part"), entity, kleene);
            case CLOSE:
            case BAR:
            case END:
                // eps
                return new Tree(format("Part"));
            default:
                throw new AssertionError();
        }
    }

    private Tree Kleene() throws ParseException {
        switch (lex.curToken()) {
            case STAR:
                // *
                lex.nextToken();
                return new Tree(format("Kleene"), new Tree(format("*")));
            case OPEN:
            case CLOSE:
            case BAR:
            case LETTER:
            case END:
                // eps
                return new Tree(format("Kleene"));
            default:
                throw new AssertionError();
        }
    }

    private Tree Entity() throws ParseException {
        switch (lex.curToken()) {
            case OPEN:
                // (
                lex.nextToken();
                // Reg
                Tree reg = Reg();
                // )
                if (lex.curToken() != Token.CLOSE) {
                    throw new ParseException(") expected at position ", lex.curPos());
                }
                lex.nextToken();
                return new Tree(format("Entity"), new Tree(format("(")), reg, new Tree(format(")")));
            case LETTER:
                // [a..z]
                lex.nextToken();
                return new Tree(format("Entity"), new Tree(format("Letter")));
            case CLOSE:
            case BAR:
            case END:
                // eps
                return new Tree(format("Entity"));
            default:
                throw new AssertionError();
        }
    }

    private Tree RegPrime() throws ParseException {
        switch (lex.curToken()) {
            case BAR:
                // |
                lex.nextToken();
                // Choice
                Tree choice = Choice();
                // Reg''
                Tree regPrime = RegPrime();
                return new Tree(format("RegPrime"), new Tree(format("|")), choice, regPrime);
            case CLOSE:
            case END:
                // eps
                return new Tree(format("RegPrime"));
            default:
                throw new AssertionError();
        }
    }

//    Tree RegPrimePrime() throws ParseException {
//        switch (lex.curToken()) {
//            case BAR:
//                // Reg'
//                Tree regPrime = RegPrime();
//                return new Tree("RegPrimePrime", regPrime);
//            case CLOSE:
//            case END:
//                // eps
//                return new Tree("RegPrimePrime");
//            default:
//                throw new AssertionError();
//        }
//    }

    private Tree ChoicePrime() throws ParseException {
        switch (lex.curToken()) {
            case OPEN:
            case LETTER:
                // Choice
                Tree choice = Choice();
                return new Tree(format("ChoicePrime"), choice);
            case CLOSE:
            case BAR:
            case END:
                // eps
                return new Tree(format("ChoicePrime"));
            default:
                throw new AssertionError();
        }
    }

    String format(String string) {
        return string + (++counter);
    }

    Tree parse(InputStream is) throws ParseException {
        lex = new LexicalAnalyser(is);
        lex.nextToken();
        Tree tree = Reg();
        if (lex.curToken() != Token.END) {
            throw new AssertionError();
        }
        return tree;
    }
}
