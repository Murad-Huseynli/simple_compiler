import java.util.ArrayList;

public class SDecls extends Stm{
	ArrayList<SDecl> decls = new ArrayList<>();
	
	public SDecls(ArrayList<SDecl> decls) {
		this.decls = decls;
	}
	
}

