package org.upmc.electisim.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import org.upmc.electisim.IElectable;

public class CombinatoricsUtils {
	public static <T> List<List<T>> generateCombinations(List<T> origList, int size) {
		List<T> tmp = new ArrayList<>();
		for(int i = 0; i < size; i++) {
			tmp.add(null);
		}
		return generateCombinationsAux(origList, 0, origList.size(), size, new ArrayList<List<T>>(), tmp);
	}
	
	public static <T> List<List<T>> generateCombinationsAux(List<T> candidateList, int begin, int end, int level, List<List<T>> l, List<T> tmp) {
		if(level == 0) {
			l.add(tmp);
			return l;
		}
		
		for(int i = begin; i <= (end - level); i++) {
			tmp.set(level - 1, candidateList.get(i));
			generateCombinationsAux(candidateList, i + 1, end, level - 1, l, new ArrayList<T>(tmp));
		}
		
		return l;
	}
	
	public static <T> List<List<T>> generatePermutations(List<T> l) {
		Stack<T> stack = new Stack<>();
		List<List<T>> res = new ArrayList<>();
		
		generatePermutationAux(new HashSet<>(l), stack, res);
		
		return res;
	}
	
	public static <T> void generatePermutationAux(Set<T> s, Stack<T> stack, List<List<T>> res) {
		if(s.isEmpty()) {
			res.add(Arrays.asList((T[]) stack.toArray()));
		}
		
		T[] availableItems = (T[]) s.toArray();
		for(T item : availableItems) {
			stack.push(item);
			s.remove(item);
			generatePermutationAux(s, stack, res);
			s.add(stack.pop());
		}
	}
}
