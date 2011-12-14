package sorbet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.sun.jdi.Location;

public class Step {

	public Location location;

	public Set<Variable> declaredVariables = new HashSet<Variable>();
	
	public Map<Variable, String> usedVariables = new HashMap<Variable, String>(); 
}