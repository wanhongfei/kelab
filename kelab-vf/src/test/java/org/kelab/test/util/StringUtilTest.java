package org.kelab.test.util;

import org.kelab.util.StringUtil;

/**
 * Created by hongfei.whf on 2017/5/4.
 */
public class StringUtilTest {

	public static void main(String args[]) {
		System.out.println(
				StringUtil.filterHtmlTags(
						"<p>为加强创新人才的培养，提高广大学生编程能力，使其更好地将理论与实践联系起来。由计算机学院主办的程序设计竞赛于2015年12月20日在新区图书馆一号机房顺利举行。</p>\n" +
								"\n" +
								"<p>题解地址：</p>\n" +
								"<p><a href=\"http://hate13.com/?p=742\" target=\"_blank\">http://hate13.com/?p=742</a></p>\n" +
								"<p><a href=\"http://www.qinshaoxuan.com/articles/805.html\" target=\"_blank\">http://www.qinshaoxuan.com/articles/805.html</a>\n" +
								"</p>\n" +
								"<p>本次大赛吸引了来自校内的68支队伍，共计190余名队员参赛。经过5个小时激烈的竞争，共有17支队伍获奖，具体情况如下：</p>\n" +
								"<center><b>请以下获奖同学到东六E312领取获奖证书</b></center><br/>\n" +
								"<center><img src=\"http://www.qinshaoxuan.com/wp-content/uploads/2015/12/院赛结果.png\"/></center>"
				));
	}

}
