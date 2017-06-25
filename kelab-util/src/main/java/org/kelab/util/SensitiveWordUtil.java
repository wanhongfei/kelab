package org.kelab.util;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.kelab.util.model.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by hongfei.whf on 2017/2/23.
 */
@Slf4j
public class SensitiveWordUtil {

	private static final String IsEnd = "isEnd";

	private static final HashMap<String, Object> SensitiveWordDFATree = new HashMap<>();

	/**
	 * 清空
	 */
	public static synchronized void clear() {
		SensitiveWordDFATree.clear();
	}

	/**
	 * 初始化
	 *
	 * @param words
	 */
	public static synchronized void init(@NonNull List<String> words) {
		SensitiveWordDFATree.clear();
		for (String word : words) {
			addSensitiveWord2DFATree(word);
		}
	}

	/**
	 * 将敏感词加入DFA树中
	 *
	 * @param word
	 */
	public static synchronized void addSensitiveWord2DFATree(@NonNull String word) {
		HashMap<String, Object> root = SensitiveWordDFATree;
		char[] chs = word.toCharArray();
		for (int i = 0, len = chs.length; i < len; i++) {
			if (!root.containsKey(chs[i] + "")) {
				HashMap<String, Object> newRoot = new HashMap<>();
				root.put(chs[i] + "", newRoot);
			}
			root = (HashMap) root.get(chs[i] + "");
			if (i == len - 1) root.put(IsEnd, true);
			else if (!root.containsKey(IsEnd) || Boolean.FALSE.equals(root.get(IsEnd))) {
				root.put(IsEnd, false);
			} else {
				// 不允许覆盖短的敏感词
				continue;
			}
		}
	}

	/**
	 * 过滤敏感词
	 *
	 * @param s
	 * @return
	 */
	public static boolean isContain(@NonNull String s) {
		for (int i = 0, len = s.length(); i < len; i++) {
			char ch = s.charAt(i);
			if (SensitiveWordDFATree.containsKey(ch + "")) {
				HashMap<String, Object> root = (HashMap<String, Object>) SensitiveWordDFATree.get(ch + "");
				for (int j = i + 1; j < len; j++) {
					// 已经为结束了，直接返回true
					if (root.get(IsEnd).equals(true)) return true;
					else {
						// 未结束，继续匹配
						char nextCh = s.charAt(j);
						if (!root.containsKey(nextCh + "")) break;
						else {
							root = (HashMap<String, Object>) root.get(nextCh + "");
						}
					}
				}
			} else {
				continue;
			}
		}
		return false;
	}

	/**
	 * 过滤敏感词
	 *
	 * @param s
	 * @return
	 */
	public static String filter(@NonNull String s, char rep) {
		List<Pair<Integer, Integer>> positions = new ArrayList<>();
		for (int i = 0, len = s.length(); i < len; i++) {
			char ch = s.charAt(i);
			if (SensitiveWordDFATree.containsKey(ch + "")) {
				HashMap<String, Object> root = (HashMap<String, Object>) SensitiveWordDFATree.get(ch + "");
				for (int j = i + 1; j < len; j++) {
					int startpos = i;
					// 已经为结束了，直接返回true
					if (root.get(IsEnd).equals(true)) {
						positions.add(new Pair<Integer, Integer>(startpos, j));
						break;
					} else {
						// 未结束，继续匹配
						char nextCh = s.charAt(j);
						if (!root.containsKey(nextCh + "")) break;
						else {
							root = (HashMap<String, Object>) root.get(nextCh + "");
						}
					}
				}
			} else {
				continue;
			}
		}
		char[] arr = s.toCharArray();
		for (Pair<Integer, Integer> pos : positions) {
			for (int i = pos.getValue1(); i < pos.getValue2(); i++) {
				arr[i] = rep;
			}
		}
		return new String(arr);
	}

}
