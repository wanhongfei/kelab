package org.kelab.test.util;

import org.springframework.core.ResolvableType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wanhongfei on 2016/11/18.
 */
public class BeanTest {

	public static void main(String args[]) throws InstantiationException, IllegalAccessException, NoSuchFieldException, NoSuchMethodException {
//		List<Integer> tagss = new ArrayList<Integer>();
//		tagss.add(1);
//		tagss.add(2);
//		tagss.add(3);
//		tagss.add(1);
//		Integer[] array = CollectionUtil.list2Array(tagss, Integer.class);
//		System.out.println(array.length);
//		System.out.println(CollectionUtil.array2List(array));
//		System.out.println(SettingsConstant.RESET_USER_PWD);
//		System.out.println(BeanUtil.bean2Map(new A()));
//		Map<String, Map<String, Map<String, String>>> a = new HashMap<>();
//		Map<String, Map<String, String>> b = new HashMap<>();
//		Map<String, String> c = new HashMap<>();
//		c.put("z", "1haha");
//		b.put("c", c);
//		a.put("b", b);
//		System.out.println(BeanUtil.getFieldValueByFieldNamesStr(a, "b.c.z", String.class));
//
//		Tags tags = new Tags(100, "21");
//		System.out.println(BeanUtil.getFieldValue(tags, "id", Integer.class));
//
//		ProblemWithBLOBs problemWithBLOBs = new ProblemWithBLOBs();
//		problemWithBLOBs.setDescription("1");
//		problemWithBLOBs.setFrameSource("1");
//		problemWithBLOBs.setHint("1");
//		problemWithBLOBs.setInput("1");
//		problemWithBLOBs.setOutput("1");
//		problemWithBLOBs.setSampleOutput("1");
//		problemWithBLOBs.setSampleInput("1");
//		problemWithBLOBs.setSource("1");
//		problemWithBLOBs.setAcNum(1);
//		problemWithBLOBs.setMemoryLimit(1);
//		problemWithBLOBs.setTimeLimit(1);
//		problemWithBLOBs.setTitle("1");
//		problemWithBLOBs.setEnabled(true);
//		problemWithBLOBs.setSpecialJudge(true);
//		problemWithBLOBs.setSubmitNum(1);
//		problemWithBLOBs.setTraining(true);
//		problemWithBLOBs.setType(1);
//		problemWithBLOBs.setTags("1,2,3,4,5,6,7,8,9");
//		problemWithBLOBs.setId(10002);
//		System.out.println(BeanUtil.getFieldValue(problemWithBLOBs, "id", Integer.class));
//		BeanUtil.setFieldValue(problemWithBLOBs, "id", 10003);
//		System.out.println(problemWithBLOBs.getId());
//		SensitiveWord word = new SensitiveWord("hello");
//		System.out.println(BeanUtil.getFieldValue(word, "word", String.class));
//
//		System.out.println(StringEscapeUtils.escapeHtml4("A%20+%20B"));
//		System.out.println(BeanUtil.isWrapClass(1));
//		System.out.println(BeanUtil.isWrapClass(1.0));
//		System.out.println(BeanUtil.isWrapClass("1"));
//		System.out.println(BeanUtil.isWrapClass('a'));
//		System.out.println(BeanUtil.isWrapClass(new A()));
//		Map<String, Object> map = BeanUtil.bean2Map(new A());
//		System.out.println(map);
//		System.out.println(BeanUtil.map2Bean(map));

		Map<String, List<Integer>> listMap = new HashMap<>();


		Class returnType = listMap.getClass();
		System.out.println(returnType);
		ResolvableType type = ResolvableType.forMethodReturnType(BeanTest.class.getMethod("test"));
		// Map<K,T> KT的类型，如果entityType==null,说明是无法解析，代表是basedao里面的方法
		System.out.println(type.getGeneric(0).getRawClass());
		System.out.println(type.getGeneric(1).getRawClass());
		System.out.println(type.getGeneric(1, 0).getRawClass());
	}

	public Map<String, List<Integer>> test() {
		return null;
	}
}
