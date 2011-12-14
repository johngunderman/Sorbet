package sorbet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.sun.jdi.Location;

public class Step {

	public Location location;

	public Set<String> declaredVariables = new HashSet<String>();
	
	public Map<String, String> usedVariableValues = new HashMap<String, String>(); 
}