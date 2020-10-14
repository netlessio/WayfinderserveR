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
    private static HttpCache<S