package sorbet;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

public class ValidLogger implements IParameterValidator {
	public void validate(String name, String value)	throws ParameterException {
		if (!value.equals("console") && !value.equals("sqlite")) {
			throw new ParameterException("Parameter " + name + " must be valid (console or sqlite)");
		}
	}
}
