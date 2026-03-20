package com.mx.PruebaChakray.Service;

import java.util.List;

public interface IMethods {
	Object create (Object obj) throws Exception;
	void update(String id, Object obj) throws Exception;
	void delete(String id) throws Exception;
	
	Object find(String id) throws Exception;
	List<Object> list();
}
