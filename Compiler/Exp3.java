
public class Exp3 {
	NumTypes numTypes;
	Id id;
	Exp exp;
	FuncCall funcCall;

	Exp3(NumTypes numTypes) {
		this.numTypes = numTypes;
	}

	Exp3(Id id) {
		this.id = id;
	}

	Exp3(Exp exp) {
		this.exp = exp;
	}

	Exp3(FuncCall funcCall) {
		this.funcCall = funcCall;
	}
}
