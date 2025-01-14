
public class SIf extends Stm{
	Exp exp; 
	Stm stm;
	IRest irest;
	
	public SIf(Exp exp, Stm stm, IRest irest) {
		this.exp = exp;
		this.stm = stm;
		this.irest = irest;
	}
	
}
