
public class SAssign extends Stm {
    Id id;
    Exp expression;
    Id assignedId;
    
    public SAssign(Id id, Exp expression, Id assignedId) {
        this.id = id;
        this.expression = expression;
        this.assignedId = assignedId;
    }
      
}