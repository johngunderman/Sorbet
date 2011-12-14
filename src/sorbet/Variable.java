package sorbet;

public class Variable {
		
	public String prefix = null;
	
	public String name = null;

	public boolean isLocal = false;
	
	public boolean isStatic = false;
	
	public long classID = -1;
	
	public Variable(String name) {
		this.name = name;
	}
	
	public String getFullName() {
		if (prefix == null) {
			return name;
		} else {
			return prefix + "." + name;
		}
	}
	
	@Override
	public int hashCode() {
		//System.out.println("CCCCCCCCCCCCCCCCCCCCCCCCCCCC");
		return toString().hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		//System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		if (o instanceof Variable) {
			Variable v = (Variable)o;
			return this.isLocal == v.isLocal &&
				   this.isStatic == v.isStatic &&
				   this.classID == v.classID &&
				   ((this.prefix == null && v.prefix ==null) || (this.prefix != null && v.prefix != null && this.prefix.equals(v.prefix))) &&
				   this.name.equals(v.name);
		}
		return false;
	}
	
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		
		if (prefix != null) {
			buffer.append(prefix);
			buffer.append(".");
		}
		
		buffer.append(name);
		
		if (isLocal) {
			buffer.append(" (local)");
		} else if (isStatic) {
			buffer.append(" (static)");
		} else {
			buffer.append(" (class id = ");
			buffer.append(classID);
			buffer.append(")");
		}
		
		return buffer.toString();
	}
}
