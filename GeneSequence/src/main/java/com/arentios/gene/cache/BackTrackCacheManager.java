package com.arentios.gene.cache;

import java.util.ArrayList;
import java.util.Set;

import org.apache.commons.jcs.JCS;
import org.apache.commons.jcs.access.CacheAccess;
import org.apache.commons.jcs.engine.control.CompositeCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.arentios.gene.domain.BackTrackData;

/**
 * Basic JCS cache manager system for back track data to alleviate memory issues with back tracking
 * @author Arentios
 *
 */
public class BackTrackCacheManager {

	
	private static final String BACKTRACK_CACHE = "backTrackCache";
	
	private static CacheAccess<Integer, ArrayList<BackTrackData>> backtrackCache;
	private static Logger LOGGER = LoggerFactory.getLogger(BackTrackCacheManager.class);
	private static BackTrackCacheManager instance;
	private static Integer currentIndex = 0;
	
	private BackTrackCacheManager(){
		
		backtrackCache = JCS.getInstance(BACKTRACK_CACHE);
	}
	
	public static BackTrackCacheManager getInstance(){
		if (instance == null){
			instance = new BackTrackCacheManager();
		}
		return instance;
	}
	
	public boolean putBackTrackData(ArrayList<BackTrackData> data){
		try{
			backtrackCache.put(currentIndex, data);
			currentIndex++;
		}catch(Exception e){
			LOGGER.error(e.getMessage());
			return false;
		}
		return true;
	}
	
	public ArrayList<BackTrackData> getBackTrackData(Integer id){
		try{
			ArrayList<BackTrackData> data = backtrackCache.get(id);
			backtrackCache.remove(id);
			return data;
		}catch(Exception e){
			LOGGER.error(e.getMessage());
			return null;
		}

	}
	
	/**
	 * Return an array list of all currently used IDs from the cache
	 * @return
	 */
	public ArrayList<Integer> getAllKeys(){
		Set<Object> rawKeys = CompositeCacheManager.getInstance().getCache(BACKTRACK_CACHE).getMemoryCache().getKeySet();
		ArrayList<Integer> results = new ArrayList<Integer>();
		for(Object o : rawKeys){
			results.add((Integer) o);
		}	
		return results;
		
		
	}

}
