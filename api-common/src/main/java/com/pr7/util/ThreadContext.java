package com.pr7.util;

import java.util.HashMap;
import java.util.Map;

public class ThreadContext {	
	private Map<String, Object> content = new HashMap<String, Object>();

	public Map<String, Object> getDataMap() {
		return content;
	}

	public void setContent(Map<String, Object> content) {
		this.content = content;
	}
	
}
