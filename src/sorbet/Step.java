package sorbet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.sun.jdi.Location;

public class Step {

	public Location location;

	public Set<String> knownVariables = new HashSet<String>();
	
	public Map<String, String> variableValues = new HashMap<String, String>(); 
}