import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Lexer {

	public static ArrayList<Token> lex(String input) {
		ArrayList<Token> tokens = new ArrayList<>();

		StringBuilder tokenPatternsBuffer = new StringBuilder();
		for (TokenType tokenType : TokenType.values()) {
			tokenPatternsBuffer.append(String.format("|(?<%s>%s)", tokenType.name(), tokenType.pattern));
		}

		Pattern tokenPatterns = Pattern.compile(tokenPatternsBuffer.substring(1));

		Matcher matcher = tokenPatterns.matcher(input);
		while (matcher.find()) {
			for (TokenType tokenType : TokenType.values()) {
				String tokenName = tokenType.name();
				if (matcher.group(tokenName) != null) {
					tokens.add(new Token(tokenType, matcher.group(tokenName), matcher.start(tokenName)));
				}
			}
		}

		return tokens;
	}

	public static void main(String[] args) {
		String input = """
				int main()
				{
				  // This is the main function
				  float a = 0.1, b = 7.93, c = -0.94;
				  int d = 4, e = -6, f = 0;
				  string s1 = "HELLO", s2 = "THERE", s3 = "TOKENIZE", s4 = "ME\n";
				
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

		ArrayList<Token> tokens = lex(input);
		for (Token token : tokens) {
			System.out.println("(" + token.getType() + " " + token.getData() + " " + token.getPosition() + ")");
		}
	}
}