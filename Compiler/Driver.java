import java.util.*;

public class Driver {
    public static void main(String[] args) throws SyntaxErrorException {

        String input = """
                int main()
                {
                  // This is the main function
                  float a = 0.1, b = 7.93, c = -0.94;
                  int d = 4, e = -6, f = 0;
                  string s1 = "HELLO", s2 = "THERE", s3 = "TOKENIZE", s4 = "ME";

                  a = a + d;
                  b = c - e;
                  c = b * 5;
                  d = (a + b) * 2 % 5;
                  e = (7 + d) / 4 * 2;
                  f = 6 % e * 4 / 7;

                  if (a >= b) print(a);
                  else print(b);
                  if (e <= f || 5 != 4) print(e);
                  if (!(c > d) ^ d != c || a < b) print(c);
                  else print(d);

                  while (a != d)
                  {
                    if (a > b) print(b);
                    a = a - 2;
                  }

                  return e;
                }
                            """;
        try {
            ArrayList<Token> tokens = Lexer.lex(input);
            // System.out.println(tokens);
            Parser parser = new Parser(tokens);
            Program program = parser.parsePrg();

            System.out.println("***Parsing completed successfully!***");

            Evaluate evaluator = new Evaluate();

            evaluator.evaluateProgram(program);
        } catch (SyntaxErrorException e) {
            System.err.println("***Syntax Error: ***" + e.getMessage());
        }
    }
}
