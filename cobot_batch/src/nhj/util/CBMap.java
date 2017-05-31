package nhj.util;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class CBMap implements Map {
	
	private Map m = null;
	
	public CBMap(Map paramMap){
		m = paramMap;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return m.size();
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return m.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		// TODO Auto-generated method stub
		return m.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		// TODO Auto-generated method stub
		return m.containsValue(value);
	}

	@Override
	public Object get(Object key) {
		return m.get(key);
	}
	
	public String getString(Object key){
		Object oKey = m.get(key);
		if( oKey == null ) return "";
		return oKey.toString();
	}
	
		
	public BigDecimal getDecimal(Object key){
		Object oKey = m.get(key);		
		BigDecimal rtnDecimal = BigDecimal.ZERO;
		if( oKey == null ) return rtnDecimal;
		try {
			rtnDecimal = new BigDecimal(oKey.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rtnDecimal;
	}
	
	public int getInt(Object key){
		Object oKey = m.get(key);		
		int rtnInt = 0;
		if( oKey == null ) return rtnInt;
		try {
			rtnInt = Integer.parseInt(oKey.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rtnInt;
	}
	

	@Override
	public Object put(Object key, Object value) {
		// TODO Auto-generated method stub
		return m.put(key, value);
	}

	@Override
	public Object remove(Object key) {
		// TODO Auto-generated method stub
		return m.remove(key);
	}

	@Override
	public void putAll(Map m) {
		// TODO Auto-generated method stub
		m.putAll(m);
	}

	@Override
	public void clear() {
		m.clear();
		
	}

	@Override
	public Set keySet() {
		// TODO Auto-generated method stub
		return m.keySet();
	}

	@Override
	public Collection values() {
		// TODO Auto-generated method stub
		return m.values();
	}

	@Override
	public Set entrySet() {
		// TODO Auto-generated method stub
		return m.entrySet();
	}
}
