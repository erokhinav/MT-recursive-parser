import java.io.InputStream;
import java.text.ParseException;

public class Parser {
    LexicalAnalyser lex;

    private Tree Reg() throws ParseException {
        switch (lex.curToken()) {
            case OPEN:
            case LETTER:
                // Choice
                Tree choice = Choice();
                // Reg'
                Tree regPrime = RegPrime();
                return new Tree("Reg", choice, regPrime);
            case BAR:
                // Reg'
                Tree regPrime1 = RegPrime();
                return new Tree("Reg", regPrime1);
            case CLOSE:
            case END:
                // eps
                return new Tree("Reg");
            default:
                throw new ParseException("Invalid token " + lex.curToken() + " in Reg()", lex.curPos());
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
                return new Tree("Choice", part, choicePrime);
            case CLOSE:
            case BAR:
            case END:
                // eps
                return new Tree("Choice");
            default:
                throw new ParseException("Invalid token " + lex.curToken() + " in Choice()", lex.curPos());
        }
    }

    private Tree Part() throws ParseException {
        switch (lex.curToken()) {
            case OPEN:
            case LETTER:
                // Entity
                Tree entity = Entity();
                // Kleene
                Tree kleene = Unary();
                return new Tree("Part", entity, kleene);
            case CLOSE:
            case BAR:
            case END:
                // eps
                return new Tree("Part");
            default:
                throw new ParseException("Invalid token " + lex.curToken() + " in Part()", lex.curPos());
        }
    }

    private Tree Unary() throws ParseException {
        switch (lex.curToken()) {
            case STAR:
                // *
                lex.nextToken();
                return new Tree("Kleene", new Tree("*"));
            case PLUS:
                // +
                lex.nextToken();
                return new Tree("Kleene", new Tree("+"));
            case OPEN:
            case CLOSE:
            case BAR:
            case LETTER:
            case END:
                // eps
                return new Tree("Kleene");
            default:
                throw new ParseException("Invalid token " + lex.curToken() + " in Kleene()", lex.curPos());
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
                return new Tree("Entity", new Tree("("), reg, new Tree(")"));
            case LETTER:
                // [a..z]
                lex.nextToken();
                return new Tree("Entity", new Tree("Letter"));
            case CLOSE:
            case BAR:
            case END:
                // eps
                return new Tree("Entity");
            default:
                throw new ParseException("Invalid token " + lex.curToken() + " in Entity()", lex.curPos());
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
                return new Tree("RegPrime", new Tree("|"), choice, regPrime);
            case CLOSE:
            case END:
                // eps
                return new Tree("RegPrime");
            default:
                throw new ParseException("Invalid token " + lex.curToken() + " in RegPrime()", lex.curPos());
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
                return new Tree("ChoicePrime", choice);
            case CLOSE:
            case BAR:
            case END:
                // eps
                return new Tree("ChoicePrime");
            default:
                throw new ParseException("Invalid token " + lex.curToken() + " in ChoicePrime()", lex.curPos());
        }
    }

    Tree parse(InputStream is) throws ParseException {
        lex = new LexicalAnalyser(is);
        lex.nextToken();
        Tree tree = Reg();
        if (lex.curToken() != Token.END) {
            throw new AssertionError("Reached end of expression");
        }
        return tree;
    }
}
