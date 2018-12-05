import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;

public class LexicalAnalyser {
    private InputStream is;
    private int curChar;
    private int curPos;
    private Token curToken;

    public LexicalAnalyser(InputStream is) throws ParseException {
        this.is = is;
        curPos = 0;
        nextChar();
    }

    private void nextChar() throws ParseException {
        curPos++;
        try {
            curChar = is.read();
        } catch (IOException e) {
            throw new ParseException(e.getMessage(), curPos);
        }
    }

    public void nextToken() throws ParseException {
        switch (curChar) {
            case '|':
                nextChar();
                curToken = Token.BAR;
                break;
            case '*':
                nextChar();
                curToken = Token.STAR;
                break;
            case '+':
                nextChar();
                curToken = Token.PLUS;
                break;
            case '(':
                nextChar();
                curToken = Token.OPEN;
                break;
            case ')':
                nextChar();
                curToken = Token.CLOSE;
                break;
            case -1:
                curToken = Token.END;
                break;
            default:
                if (Character.isLowerCase(curChar)) {
                    nextChar();
                    curToken = Token.LETTER;
                } else {
                    throw new ParseException("Illegal character " + (char) curChar, curPos);
                }
        }
    }

    public Token curToken() {
        return curToken;
    }

    public int curPos() {
        return curPos;
    }
}
