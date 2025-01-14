public class Token {
		private TokenType type;
		private String data;
		private int position;

		public Token(TokenType type, String data, int position) {
			this.type = type;
			this.data = data;
			this.position = position;
		}

		public TokenType getType() {
			return type;
		}

		public String getData() {
			return data;
		}

		public int getPosition() {
			return position;
		}

		@Override
		public String toString() {
			return String.format("(%s %s)", type.name(), data);
		}
	}
