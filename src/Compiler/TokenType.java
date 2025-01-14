public enum TokenType {
		// Token types cannot have underscores
		COMMENT("(//.*)|/\\*([^*]|\\*+[^*/])*\\*+/"),
		
		TYPEFLOAT("float"),
		TYPEINT("int"),
		TYPESTRING("string"), 
		
		FLOAT("-?([0-9]+\\.[0-9]*)|([0-9]*\\.[0-9]+)"),
		INT("-?[0-9]+"),
		STRING("\"(?:\\\\.|[^\"\\\\])*\""),
		 
	    PLUS("\\+"),
		MINUS("-"),
		MULTIPLICATION("\\*"),
		DIVISION("/"),
		MOD("%"), 
		
		GREATEREQUAL(">="),
		GREATER(">"),
		LESSEQUAL("<="),
		LESS("<"),
		NOTEQUAL("!="),
		OR("(\\|\\|)|(\\|)|(or)"),
		AND("(&&)|(&)|(and)"),
		XOR("(\\^)|(xor)"),
		NOT("(!)|(not)"),
		EQUAL("\\=="),
		
		ASSIGN("="),
		ELSEIF("else if"),
		IF("if"),
		ELSE("else"),
		WHILE("while"),
		LEFTCURLYBRACKET("\\{"),
		RIGHTCURLYBRACKET("\\}"),
		LEFTBRACKET("\\("),
		RIGHTBRACKET("\\)"),
		COMMA(","),
		SEMICOLON(";"),
		RETURN("return"),
		IDENT("[a-zA-Z_$][a-zA-Z0-9_$]*");

		public final String pattern;

		private TokenType(String pattern) {
			this.pattern = pattern;
		}

	}
