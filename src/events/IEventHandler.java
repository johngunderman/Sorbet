package events;

import com.sun.jdi.event.Event;

public interface IEventHandler {
	
	public int handle(Event event);
	
}
