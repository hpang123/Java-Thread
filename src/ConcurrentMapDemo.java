import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ConcurrentMapDemo {

	public static void main(String[] args) {
		ConcurrentMap<String, String> map = new ConcurrentHashMap<>();
		map.put("foo", "bar");
		map.put("han", "solo");
		map.put("r2", "d2");
		map.put("c3", "p0");
		
		/*
		 * The iteration is performed sequentially on the current thread.
		 */
		map.forEach((key, value) -> System.out.printf("%s = %s\n", key, value));

		String value = map.putIfAbsent("c3", "p1");
		System.out.println(value);    // p0
		value = map.putIfAbsent("c4", "p1");
		System.out.println(map.get("c4"));    // p1

		value = map.getOrDefault("hi", "there");
		System.out.println(value);    // there

		value = map.getOrDefault("c4", "there");
		System.out.println(value);    // p1
		
		map.replaceAll((key, v) -> "r2".equals(key) ? "d3" : v+"9");
		System.out.println(map.get("r2"));    // d3
		
		map.forEach((key, v) -> System.out.printf("%s = %s\n", key, v));
		
		//transfer key foo's value
		map.compute("foo", (key, v) -> v.substring(0, v.length()-1) + v.substring(0, v.length()-1));
		System.out.println(map.get("foo"));   // barbar

		map.forEach((key, v) -> System.out.printf("%s = %s\n", key, v));
		
		//change foo key's value to boo + "was " + barbar
		map.merge("foo", "boo", (oldVal, newVal) -> newVal + " was " + oldVal);
		System.out.println(map.get("foo"));   // boo was barbar

	}

}
