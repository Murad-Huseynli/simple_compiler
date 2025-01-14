import java.util.HashMap;
import java.util.Stack;

public class State {
    HashMap<String, Stack<Data>> state = new HashMap<String, Stack<Data>>();

	public Data get(String identifier) {
        if (state.containsKey(identifier) && !state.get(identifier).isEmpty()) {
            return state.get(identifier).peek();
        }
        System.out.println(identifier + " does not exist");
        return null;
    }

    public void updateValue(String ident, Data v) throws TypeException {
        if (state.containsKey(ident) && !state.get(ident).isEmpty()) {
            state.get(ident).pop();
            state.get(ident).push(v);
        } else {
            System.out.println(ident + " not initialized");
        }
    }

    public void addIdent(String ident, Data v) {
        Stack<Data> s = new Stack<Data>();
        // Adding default value automatically
        s.push(v);
        state.put(ident, s);
    }

    public void pushValue(String ident, Data v) {
        state.get(ident).push(v);
    }

    public void popValue(String ident) {
        state.get(ident).pop();
    }

    public void printAllValues() {
        for (String identifier : state.keySet()) {
            Stack<Data> valueStack = state.get(identifier);
            System.out.print(identifier + ": ");
            for (Data value : valueStack) {
                System.out.print(value.getValue() + " ");
            }
            System.out.println();
        }
    }
}
