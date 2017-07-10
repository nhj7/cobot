package nhj.util;

import java.util.List;
import java.util.Map;

public class PrintUtil {
	public static void printList( List list ){
		
		if( list == null ){
			System.out.println("list is null");
			return;
		}
		
		for(int i = 0; i < list.size();i++){
			System.out.println(( i + 1 ) + " : " + list.get(i));
			
		}
	}
	
	public static void printMap( Map m ){
		
		for( java.util.Iterator it = m.keySet().iterator();it.hasNext();){
			Object key = it.next();
			System.out.println("key : " + key + ", value : " + m.get(key));
		}
		
		
	}
}
