package org.upmc.electisim.utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.upmc.electisim.Candidate;

public class MapUtils {
	public static <K, V extends Comparable<V>> List<Map.Entry<K, V>> sortByValue(Map<K, V> map) {
		List<Map.Entry<K, V>> set = new ArrayList<>(map.entrySet());
		set.sort(Map.Entry.comparingByValue(new Comparator<V>() {

			@Override
			public int compare(V arg0, V arg1) {
				return arg0.compareTo(arg1);
			}
			
		}));
		
		return set;
	}
}
