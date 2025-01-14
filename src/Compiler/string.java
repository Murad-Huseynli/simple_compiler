
public class string extends Data {
    public String s;
    
    public string(String str) {
        s = str;
    }
    
    public String getType() {
        return "string";
    }
    
    public Object getValue() {
        return s;
    }
}