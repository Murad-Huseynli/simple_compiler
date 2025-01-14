import java.util.ArrayList;

public class FuncDef {
	Type type;
	Id id;
	ArrayList<Param> params;
	ArrayList<Stm> stms;
	
	public FuncDef(Type type, Id id, ArrayList<Param> params, ArrayList<Stm> stms) {
		this.type = type;
		this.id = id;
		this.params = params;
		this.stms = stms;
	}
	
}
