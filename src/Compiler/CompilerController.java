import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CompilerController {

    @PostMapping("/compile")
    @ResponseBody
    public String compileCode(@RequestBody String code) {
        try {
            ArrayList<Token> tokens = Lexer.lex(code);
            Parser parser = new Parser(tokens);
            Program program = parser.parsePrg();

            Evaluate evaluator = new Evaluate();
            evaluator.evaluate(program);

            return "Compilation and evaluation completed successfully!";
        } catch (SyntaxErrorException e) {
            return "Syntax Error: " + e.getMessage();
        }
    }
}
