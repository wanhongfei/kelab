package org.kelab.test.util;

import org.junit.Test;

import java.io.IOException;

public class FastJsonUtilTest {

	@Test
	public void transJson() throws IOException {
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
//		Tags tags = new Tags();
//		tags.setName("nima1");
//		Map<Tags, ProblemWithBLOBs> obj = new HashMap<>();
//		obj.put(tags, problemWithBLOBs);
//		String json = FastJsonUtil.object2Json(obj);
//		System.out.println(json);
//		Map<Tags, ProblemWithBLOBs> obj1 = FastJsonUtil.json2Map(json,
//				Tags.class, ProblemWithBLOBs.class);
//		for (Map.Entry<Tags, ProblemWithBLOBs> entry : obj1.entrySet()) {
//			System.out.println(entry.getKey().getName());
//			System.out.println(entry.getValue().getTitle());
//		}
//		System.out.println(obj1);'
//		Map<Integer, ProblemWithBLOBs> map = new HashMap<>();
//		map.put(22, problemWithBLOBs);
//		String json = FastJsonUtil.object2Json(map);
//
//		Map<Integer, ProblemWithBLOBs> map1 = FastJsonUtil.json2Map(json, Integer.class, ProblemWithBLOBs.class);
//		System.out.println(map1);

//		String[] arr = new String[]{"1", "2"};
//		String json1 = FastJsonUtil.object2Json(arr);
//		System.out.println(json1);
//		System.out.println(FastJsonUtil.json2Array(json1, String.class));

//		ProblemWithBLOBs[] arr = new ProblemWithBLOBs[]{problemWithBLOBs};
//		String json1 = FastJsonUtil.object2Json(arr);
//		System.out.println(json1);
//		System.out.println(FastJsonUtil.json2Array(json1, ProblemWithBLOBs.class));

//		String json = FastJsonUtil.object2Json(problemWithBLOBs);
//		System.out.println(FastJsonUtil.json2Object(json, ProblemWithBLOBs.class));

//      System.out.println(FastJsonUtil.object2Json(problemWithBLOBs));
//		Map<String, Object> map = new HashMap<>();
//		SiteSettingsExt siteSettings = new SiteSettingsExt(3, "RESET_PWD_MAIL_EXP", "15");
//		map.put("hello", siteSettings);
//		String json = FastJsonUtil.object2Json(map);
//		SiteSettings settings = FastJsonUtil.json2Map(json, SiteSettings.class).get("hello");
//		System.out.println(settings.getClass());
//
//		List<Integer> list = Arrays.asList(new Integer[]{1, 2, 3});
//		json = FastJsonUtil.object2Json(list);
//		System.out.println(FastJsonUtil.json2List(json, Integer.class).get(0).getClass());
//
//		json = FastJsonUtil.object2Json(siteSettings);
//		System.out.println(FastJsonUtil.json2Object(json, SiteSettings.class).getClass());
	}

}
