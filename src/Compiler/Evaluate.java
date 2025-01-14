import java.util.HashMap;
import java.util.Stack;

public class Evaluate {
	private State state;

	public Evaluate() {
		state = new State();
	}

	public void evaluateProgram(Program program) {
		for (FuncDef funcDef : program.definitions) {
			evaluateFuncDef(funcDef);
		}
	}

	public void evaluateFuncDef(FuncDef funcDef) {
		if (funcDef.params != null) {
			for (Param param : funcDef.params) {
				Type type = param.type;
				Id identifier = param.id;
				if (type instanceof INTType) {
					state.addIdent(identifier.id, new integer(0));
				} else if (type instanceof FLOATType) {
					state.addIdent(identifier.id, new Float(0.0));
				} else if (type instanceof STRINGType) {
					state.addIdent(identifier.id, new string(""));
				}
			}
		}

		if (funcDef.stms != null) {
			for (Stm stm : funcDef.stms) {
				evaluateStm(stm);
			}
		}
	}

	public void evaluateStm(Stm stm) {
		if (stm instanceof SExp) {
			Exp exp = ((SExp) stm).exp;
			evalExp(exp);
		} else if (stm instanceof SDecl) {
			Type type = ((SDecl) stm).type;
			Id identifier = ((SDecl) stm).id;
			if (type instanceof INTType) {
				state.addIdent(identifier.id, new integer(0));
			} else if (type instanceof FLOATType) {
				state.addIdent(identifier.id, new Float(0.0));
			} else if (type instanceof STRINGType) {
				state.addIdent(identifier.id, new string(""));
			}
		} else if (stm instanceof SDecls) {
			Type type = ((SDecls) stm).decls.get(0).type;
			for (SDecl id : ((SDecls) stm).decls) {
				if (type instanceof INTType) {
					state.addIdent(id.id.id, new integer(0));
				} else if (type instanceof FLOATType) {
					state.addIdent(id.id.id, new Float(0.0));
				} else if (type instanceof STRINGType) {
					state.addIdent(id.id.id, new string(""));
				}
			}
		} else if (stm instanceof SAssign) {
			Id identifier = ((SAssign) stm).id;
			Exp exp = ((SAssign) stm).expression;
			Data value = evalExp(exp);
			state.updateValue(identifier.id, value);
		} else if (stm instanceof SInit) {
			Type type = ((SInit) stm).type;
			Id identifier = ((SInit) stm).id;
			Exp exp = ((SInit) stm).exp;
			Data value = evalExp(exp);
			if (type instanceof INTType && value instanceof integer) {
				state.addIdent(identifier.id, value);
			} else if (type instanceof FLOATType && value instanceof Float) {
				state.addIdent(identifier.id, value);
			} else if (type instanceof STRINGType && value instanceof string) {
				state.addIdent(identifier.id, value);
			} else {
				System.out.println("Invalid assignment: Cannot assign " + value.getType() + " to " + type);
			}
		} else if (stm instanceof SReturn) {
			Exp exp = ((SReturn) stm).exp;
			if (exp != null) {
				Data value = evalExp(exp);
				// Handle return value if needed
			}
			// Handle return statement
		} else if (stm instanceof SWhile) {
			Exp exp = ((SWhile) stm).getExp();
			while (evalCondition(exp)) {
				for (Stm bodyStm : ((SWhile) stm).getStm()) {
					evaluateStm(bodyStm);
				}
			}
		} else if (stm instanceof SBlock) {
			for (Stm blockStm : ((SBlock) stm).bs) {
				evaluateStm(blockStm);
			}
		} else if (stm instanceof SIf) {
			Exp exp = ((SIf) stm).exp;
			if (evalCondition(exp)) {
				for (Stm ifStm : ((SIf) stm).stm) {
					evaluateStm(ifStm);
				}
			} else {
				for (Stm elseStm : ((SIf) stm).stm) {
					evaluateStm(elseStm);
				}
			}
		}

		// Handle other types of statements
	}

	public Data evalExp(Exp exp) {
		if (exp instanceof Exp1) {
			return evalExp1((Exp1) exp);
		}
		return null;
	}

	private Data evalExp1(Exp1 exp1) {
		Data result = evalExp2(exp1.getExp2());
		if (exp1.getRExp1() != null) {
			RExp1 rexp1 = exp1.getRExp1();
			while (rexp1 != null) {
				BinOp op = rexp1.getBinOp();
				Exp2 exp2 = rexp1.getExp2();
				Data value = evalExp2(exp2);
				result = performBinaryOperation(result, op, value);
				rexp1 = rexp1.getRExp1();
			}
		}
		return result;
	}

	private Data evalExp2(Exp2 exp2) {
		if (exp2 instanceof Exp3) {
			return evalExp3((Exp3) exp2);
		}
		return null;
	}

	private Data evalExp3(Exp3 exp3) {
		if (exp3 instanceof Num) {
			return evalNum((Num) exp3);
		} else if (exp3 instanceof Id) {
			return evalId((Id) exp3);
		} else if (exp3 instanceof FuncCall) {
			return evalFuncCall((FuncCall) exp3);
		} else if (exp3 instanceof ParenExp) {
			return evalExp(((ParenExp) exp3).getExp());
		}
		return null;
	}

	private Data evalNum(Num num) {
		if (num instanceof integer) {
			int value = ((integer) num).i;
			return new integer(value);
		} else if (num instanceof Float) {
			double value = ((Float) num).f;
			return new Float(value);
		}
		return null;
	}

	private Data evalId(Id id) {
		String name = id.id;
		return state.get(name);
	}

	private Data evalFuncCall(FuncCall funcCall) {
		Id id = funcCall.id;
		String name = id.id;
		// Retrieve the function definition by name and evaluate it
		// Handle function call arguments if needed
		return null;
	}

	private boolean evalCondition(Exp exp) {
		Data data = evalExp(exp);
		if (data instanceof BooleanData) {
			return ((BooleanData) data).getValue();
		}
		return false;
	}

	private Data performBinaryOperation(Data first, BinOp op, Data second) {
		// Perform binary operations based on the types of the operands
		// Handle addition, subtraction, multiplication, division, etc.
		return null;
	}
}
