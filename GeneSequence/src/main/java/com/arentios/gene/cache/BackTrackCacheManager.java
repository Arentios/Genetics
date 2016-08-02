package com.arentios.gene.cache;

import java.util.ArrayList;
import java.util.LinkedList;
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
	
	private static CacheAccess<Integer, LinkedList<BackTrackData>> backtrackCache;
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
	
	public boolean putBackTrackData(LinkedList<BackTrackData> data){
		try{
			LOGGER.info("Adding data with size="+data.size()+" with id="+currentIndex);
			backtrackCache.put(currentIndex, data);
			currentIndex = currentIndex+1;
		}catch(Exception e){
			LOGGER.error(e.getMessage());
			return false;
		}
		return true;
	}
	
	public LinkedList<BackTrackData> getBackTrackData(Integer id){
		try{
			//Back track information can only be retrieved once, then it's removed from the cache
			LinkedList<BackTrackData> data = backtrackCache.get(id);
			LOGGER.info("Retrieved data with id="+id+" and size="+data.size());
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
	
	/**
	 * Return the first key from the cache for cases where processing order isn't important and we just want to grab something
	 * @return
	 */
	public Integer getKey(){
		Set<Object> rawKeys = CompositeCacheManager.getInstance().getCache(BACKTRACK_CACHE).getMemoryCache().getKeySet();
		for(Object o : rawKeys){
			return (Integer) o;
		}
		//If no keys, return nothing
		return null;
		
		
		
	}

}
