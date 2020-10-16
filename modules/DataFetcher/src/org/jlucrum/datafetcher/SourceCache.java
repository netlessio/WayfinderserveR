package org.jlucrum.datafetcher;

/**
 *
 * @author Evgeni Kappinen
 */

import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class SourceCache { 

    private static final long serialVersionUID = 1L;
    private static SourceCache instance;
    private static HttpCache<String, HttpCache<String, Object>> cache;

    public class HttpCache<K, V> extends LinkedHashMap<K, V> {
        private int maxSize = 50;

        public HttpCache(int maxSize) {
            super(maxSize + 1, 1, true);
            this.maxSize = maxSize;
        }

        @Override
        protected boolean removeEldestEntry(Entry<K, V> entry) {
            return size() > this.maxSize;
        }
    }

    protected void initCache(int maxSize) {
        cache = new HttpCache<String,HttpCache<String,Object>>(maxSize);
    }

    public synchronized static SourceCache getInstance(int maxSize) {

        if (instance == null) {
            instance = new SourceCache();
            instance.initCache(maxSize);
        }
        return instance;
    