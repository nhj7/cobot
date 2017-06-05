package nhj.util;

import java.util.List;

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
}
