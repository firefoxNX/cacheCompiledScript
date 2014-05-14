package com.vineetb.cache.coherence;

import java.io.Serializable;

import javax.script.CompiledScript;

public class CacheScriptEntry implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6317202741110685591L;
	private CompiledScript compiledScript;

	public CompiledScript getCompiledScript() {
		return compiledScript;
	}

	public void setCompiledScript(CompiledScript compiledScript) {
		this.compiledScript = compiledScript;
	}

}
