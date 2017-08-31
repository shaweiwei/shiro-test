package com.shiro.ehcache.factory;

import java.io.InputStream;
import org.apache.shiro.util.Factory;
import net.sf.ehcache.CacheManager;

public class SharedEhCacheManagerFactory implements Factory<CacheManager> {

	// 需要和shiro.ini配置的文件一致
	private String cacheManagerConfigFile = "/shiro-ehcache.xml";
	
	public CacheManager getInstance() {
		return CacheManager.create(readCacheManagerConfigFileAsInputStream());
	}
	
	public InputStream readCacheManagerConfigFileAsInputStream(){
        InputStream is = getClass().getResourceAsStream(cacheManagerConfigFile);
        return is;
    }

}
