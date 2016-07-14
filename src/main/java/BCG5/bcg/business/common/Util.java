package BCG5.bcg.business.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

public class Util {
	
	// To be removed
	public static <T> Predicate<T> distinctByKey(Function<? super T,Object> keyExtractor) {
	    Map<Object,Boolean> seen = new ConcurrentHashMap<>();
	    return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}
	
	public String insertHookSanitizer(StringBuilder builder){
		String builderString = builder.toString();
		builderString.replace(Constants.INSERT_HOOK, "");
		return builderString;
	}

}
