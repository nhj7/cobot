package nhj.util;

import java.util.List;

public class PrintUtil {
	public static void printList(List list){
		for(int i = 0; i < list.size();i++){
			System.out.println( (i+1) + " : " + list.get(i));
		}
	}
}
