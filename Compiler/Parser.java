import java.util.ArrayList;
import java.util.Iterator;

public class Parser {
	// list of tokens
	ArrayList<Token> tokens; // = Lexer.lex("int plus(int a, int b){ } ...");
	Iterator<Token> itr;
	Token next;
	Token before;

	public Parser(ArrayList<Token> t) {
		tokens = t;
		itr = tokens.iterator();
		next = (Token) itr.next();
		before = next;
	}

	void ignore(String s) throws SyntaxErrorException {
		if (next.getData().equals(s)) {
			// System.out.println("Ignored " + next.getData());
			if (itr.hasNext()) {
				before = next;
				next = itr.next();
			}
		} else {
			// exception
			// System.out.println("Syntax ignore error in " + next.getPosition() + " token "
			// + next.getData() + " is incorrect");
			String msg = "Syntax ignore error in " + next.getPosition() + ". Expected token is " + s;
			throw new SyntaxErrorException(next, before, msg);
			// next = itr.next();
		}
	}

	Program parsePrg() throws SyntaxErrorException {
		ArrayList<FuncDef> definitions = parseFuncDefs();
		return new Program(definitions);
	}

	ArrayList<FuncDef> parseFuncDefs() throws SyntaxErrorException {
		FuncDef f = parseFuncDef();
		ArrayList<FuncDef> defs = new ArrayList<FuncDef>();
		defs.add(f);
		while (f != null && itr.hasNext()) {
			f = parseFuncDef();
			defs.add(f);
		}
		return defs;
	}

	FuncDef parseFuncDef() throws SyntaxErrorException {
		Type t = parseType();
		if (t == null) {
			String msg = "Function type not specified in " + next.getPosition();
			throw new SyntaxErrorException(next, before, msg);
		}
		Id id = parseId();
		if (id == null) {
			String msg = "Function identifier not specified in " + next.getPosition();
			throw new SyntaxErrorException(next, before, msg);
		}
		ignore("(");
		ArrayList<Param> p = parseParams();
		ignore(")");

		ignore("{");
		ArrayList<Stm> s = parseStms();
		ignore("}");

		return new FuncDef(t, id, p, s);
	}

	Type parseType() throws SyntaxErrorException {
		if (next.getType() == TokenType.TYPEINT) {
			before = next;
			if (itr.hasNext()) {
				next = itr.next();
			}
			return new INTType();
		} else if (next.getType() == TokenType.TYPEFLOAT) {
			before = next;
			if (itr.hasNext())
				next = itr.next();
			return new FLOATType();
		} else if (next.getType() == TokenType.TYPESTRING) {
			before = next;
			if (itr.hasNext())
				next = itr.next();
			return new STRINGType();
		} else {
			String msg = "Unexpected token type in the position of " + next.getPosition();
			throw new SyntaxErrorException(next, before, msg);
		}
	}

	Id parseId() throws SyntaxErrorException {
		if (next.getType() == TokenType.IDENT) {
			before = next;
			if (itr.hasNext()) {
				next = itr.next();
			}
			return new Id(before.getData());
		} else {
			String msg = "Identifier expected in the position of " + next.getPosition();
			throw new SyntaxErrorException(next, before, msg);
		}
	}

	ArrayList<Param> parseParams() throws SyntaxErrorException {
		ArrayList<Param> params = new ArrayList<Param>();
		Param p = parseParam();
		params.add(p);
		while (p != null && itr.hasNext()) {
			p = parseParam();
			params.add(p);
		}
		return params;
	}

	Param parseParam() throws SyntaxErrorException {
		Type t = parseType();
		if (t == null) {
			String msg = "Function type not specified in " + next.getPosition();
			throw new SyntaxErrorException(next, before, msg);
		}
		Id id = parseId();
		if (id == null) {
			String msg = "Function identifier not specified in " + next.getPosition();
			throw new SyntaxErrorException(next, before, msg);
		}
		return new Param(t, id);
	}  

	ArrayList<Stm> parseStms() throws SyntaxErrorException {
		ArrayList<Stm> stms = new ArrayList<Stm>();
		Stm s = parseStm();
		stms.add(s);
		while (s != null && itr.hasNext()) {
			s = parseStm();
			stms.add(s);
		}
		return stms;
	}

	Stm parseStm() throws SyntaxErrorException {
		if (next.getData().equals("{")) {
			return parseSBlock();
		} else if (next.getData().equals("if")) {
			return parseSIf();
		} else if (next.getData().equals("while")) {
			return parseSWhile();
		} else if (next.getData().equals("return")) {
			return parseSReturn();
		} else if (next.getType() == TokenType.TYPEINT || next.getType() == TokenType.TYPEFLOAT
				|| next.getType() == TokenType.TYPESTRING) {
			if (itr.hasNext() && itr.next().getData().equals("=")) {
				return parseSInit();
			} else {
				return parseSDecl();
			}
		} else if (next.getType() == TokenType.IDENT) {
			if (itr.hasNext() && itr.next().getData().equals("=")) {
				return parseSAssign();
			} else {
				return parseSExp();
			}
		} else {
			String msg = "Invalid statement at " + next.getPosition();
			throw new SyntaxErrorException(next, before, msg);
		}
	}

	SExp parseSExp() throws SyntaxErrorException {
		Exp exp = parseExp();
		ignore(";");
		return new SExp(exp);
	}

	SAssign parseSAssign() throws SyntaxErrorException {
		Id id = parseId();
		ignore("=");
		if (next.getType() == TokenType.IDENT) {
			Id ident = parseId();
			ignore(";");
			return new SAssign(id, null, ident);
		} else {
			Exp exp = parseExp();
			ignore(";");
			return new SAssign(id, exp, null);
		}
	}

	SBlock parseSBlock() throws SyntaxErrorException {
		ignore("{");
		ArrayList<Stm> stms = parseStms();
		ignore("}");
		return new SBlock(stms);
	}

	ArrayList<SDecl> parseSDecls() throws SyntaxErrorException {
		ArrayList<SDecl> decls = new ArrayList<SDecl>();
		SDecl s = parseSDecl();
		decls.add(s);
		while (s != null && itr.hasNext()) {
			s = parseSDecl();
			decls.add(s);
		}
		return decls;
	}

	SDecl parseSDecl() throws SyntaxErrorException {
		Type t = parseType();
		if (t == null) {
			String msg = "Function type not specified in " + next.getPosition();
			throw new SyntaxErrorException(next, before, msg);
		}
		Id id = parseId();
		if (id == null) {
			String msg = "Function identifier not specified in " + next.getPosition();
			throw new SyntaxErrorException(next, before, msg);
		}
		ignore(";");
		return new SDecl(t, id);
	}

	SInit parseSInit() throws SyntaxErrorException {
		Type t = parseType();
		Id id = parseId();
		ignore("=");
		Exp exp = parseExp();
		ignore(";");
		return new SInit(t, id, exp);
	}

	SReturn parseSReturn() throws SyntaxErrorException {
		ignore("return");
		Exp exp = parseExp();
		if (exp == null) {
			String msg = "The expression is not specified in " + next.getPosition();
			throw new SyntaxErrorException(next, before, msg);
		}
		ignore(";");
		return new SReturn(exp);
	}

	SWhile parseSWhile() throws SyntaxErrorException {
		ignore("while");
		ignore("(");
		Exp exp = parseExp();
		if (exp == null) {
			String msg = "The expression is not specified in " + next.getPosition();
			throw new SyntaxErrorException(next, before, msg);
		}
		ignore(")");
		Stm stm = parseStm();
		if (stm == null) {
			String msg = "The statement is not specified in " + next.getPosition();
			throw new SyntaxErrorException(next, before, msg);
		}
		return new SWhile(exp, stm);
	}

	SIf parseSIf() throws SyntaxErrorException {
		ignore("if");
		ignore("(");
		Exp exp = parseExp();
		if (exp == null) {
			String msg = "The expression is not specified in " + next.getPosition();
			throw new SyntaxErrorException(next, before, msg);
		}
		ignore(")");
		Stm stm = parseStm();
		if (stm == null) {
			String msg = "The statement is not specified in " + next.getPosition();
			throw new SyntaxErrorException(next, before, msg);
		}
		IRest iRest = parseIRest();
		return new SIf(exp, stm, iRest);
	}

	IRest parseIRest() throws SyntaxErrorException {
		if (next.getData().equals("else")) {
			ignore("else");
			Stm stm = parseStm();
			return new IRest(stm);
		}
		return null;
	}

	Exp parseExp() throws SyntaxErrorException {
	    Exp1 exp1 = parseExp1();
	    RExp rexp = parseRExp();
	    return new Exp(exp1, rexp);
	}

	RExp parseRExp() throws SyntaxErrorException {
	    if (next.getData().equals(">")
	            || next.getData().equals("<")
	            || next.getData().equals(">=")
	            || next.getData().equals("<=")
	            || next.getData().equals("==")) {
	        BinComp binComp = parseBinComp();
	        Exp1 exp1 = parseExp1();
	        RExp rexp = parseRExp();
	        return new RExp(binComp, exp1, rexp);
	    } else {
	        return new RExp(null, null, null);
	    }
	}

	Exp1 parseExp1() throws SyntaxErrorException {
	    Exp2 exp2 = parseExp2();
	    RExp1 rexp1 = parseRExp1();
	    return new Exp1(exp2, rexp1);
	}
	
	RExp1 parseRExp1() throws SyntaxErrorException {
	    if (next.getData().equals("+") || next.getData().equals("-")) {
	        ignore(next.getData());
	        Exp2 exp2 = parseExp2();
	        RExp1 rexp1 = parseRExp1();
	        return new RExp1(exp2, rexp1);
	    } else {
	        return null;
	    }
	}

	Exp2 parseExp2() throws SyntaxErrorException {
	    Exp3 exp3 = parseExp3();
	    RExp2 rexp2 = parseRExp2();
	    return new Exp2(exp3, rexp2);
	}

	RExp2 parseRExp2() throws SyntaxErrorException {
	    if (next.getData().equals("*") || next.getData().equals("/")) {
	        ignore(next.getData());
	        Exp3 exp3 = parseExp3();
	        RExp2 rexp2 = parseRExp2();
	        return new RExp2(exp3, rexp2);
	    } else {
	        return null;
	    }
	}

	Exp3 parseExp3() throws SyntaxErrorException {
	    if (next.getType() == TokenType.INT || next.getType() == TokenType.FLOAT) {
	        NumTypes numTypes = new NumTypes(next.getData());
	        if (itr.hasNext()) {
	            next = itr.next();
	        } else {
	            next = null;
	        }
	        return new Exp3(numTypes);
	    } else if (next.getType() == TokenType.IDENT) {
	        Token current = next;
	        if (itr.hasNext()) {
	            next = itr.next();
	        } else {
	            next = null;
	        }
	        if (next != null && next.getData().equals("(")) {
	            FuncCall funcCall = parseFuncCall();
	            return new Exp3(funcCall);
	        } else {
	            Id id = new Id(current.getData());
	            return new Exp3(id);
	        }
	    } else if (next.getData().equals("(")) {
	        ignore("(");
	        Exp exp = parseExp();
	        ignore(")");
	        return new Exp3(exp);
	    } else {
	        String msg = "Invalid expression at " + next.getPosition();
	        throw new SyntaxErrorException(next, before, msg);
	    }
	}

	 
	ArrayList<Exp> parseExps() throws SyntaxErrorException {
	    ArrayList<Exp> exps = new ArrayList<>();
	    Exp exp = parseExp();
	    exps.add(exp);
	    while (next.getData().equals(",")) {
	        ignore(",");
	        exp = parseExp();
	        exps.add(exp);
	    }
	    return exps;
	}
 
	BinComp parseBinComp() throws SyntaxErrorException {
	    if (next.getData().equals(">")
	            || next.getData().equals("<")
	            || next.getData().equals(">=")
	            || next.getData().equals("<=")
	            || next.getData().equals("==")) {
	        String operator = next.getData();
	        ignore(operator);
	        return new BinComp(operator);
	    } else {
	        String msg = "Invalid binary comparison operator at " + next.getPosition();
	        throw new SyntaxErrorException(next, before, msg);
	    }
	}

	FuncCall parseFuncCall() throws SyntaxErrorException {
	    Id id = parseId();
	    ignore("(");
	    ArrayList<Exp> exps = parseExps();
	    ignore(")");
	    return new FuncCall(id, exps);
	}

}
