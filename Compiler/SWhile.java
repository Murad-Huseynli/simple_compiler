
public class SWhile extends Stm{
	private Exp exp; 
	private Stm stm;
	
	public SWhile(Exp exp, Stm stm) {
		this.exp = exp;
		this.stm = stm;
	} 
	
	public Exp getExp() {
		return exp;
	}


	public Stm getStm() {
		return stm;
	}
}
