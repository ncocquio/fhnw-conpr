package le;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

public class WhereIsMyMapEntry {
	
	public static void main(String[] args) {
		Map<Point, String> m = new HashMap<>();
		Point key = new Point(4, 2);
		
		m.put(key, "Value");
		
		System.out.println(m.get(key));

		key.x = -1;
		System.out.println(m.get(key));
		
		key.x = 4;
		System.out.println(m.get(key));
	}
}
