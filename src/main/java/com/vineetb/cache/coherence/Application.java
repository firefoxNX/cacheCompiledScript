package com.vineetb.cache.coherence;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtNewConstructor;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass;
		try {
			ctClass = pool.get("com.sun.script.javascript.RhinoCompiledScript");
		
	        CtConstructor m = CtNewConstructor.make("public RhinoCompiledScript() {}", ctClass);
	        ctClass.addConstructor(m);
	        
	        CacheFactory.ensureCluster();
	        NamedCache cache = CacheFactory.getCache("hello-example");
	        loadCache(cache);
	        CompiledScript cs88 = (CompiledScript)cache.get("script88");
//	        CompiledScript cs88 = ((CacheScriptEntry)cache.get("script88")).getCompiledScript();
        	System.out.println(cs88.eval());
		} catch (Exception e) {
			e.printStackTrace();
		}
        CacheFactory.shutdown();
    }
    
    private static void loadCache(NamedCache cache) {
    	ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("javascript");
        engine.put("counter", 0);
        if (engine instanceof Compilable) {
          Compilable compEngine = (Compilable) engine;
          try {
            CompiledScript script = null;
//            CacheScriptEntry cacheScriptEntry = null;
            for(int i=0;i<100;i++){
            	script = compEngine.compile("function count(){counter=counter+1;return counter;}; count();");
//            	cacheScriptEntry = new CacheScriptEntry();
//                cacheScriptEntry.setCompiledScript(script);
            	cache.put("script_"+i, script);
            }
//            System.out.println(script.eval());
          } catch (ScriptException e) {
            System.err.println(e);
          }
        } else {
          System.err.println("Engine can't compile code");
        }
    }
}
