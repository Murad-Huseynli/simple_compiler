
public class Float extends Data {
    public double f;
    
    public Float(double d) {
        f = d;
    }
    
    public String getType() {
        return "float";
    }
    
    public Object getValue() {
        return f;
    }
}