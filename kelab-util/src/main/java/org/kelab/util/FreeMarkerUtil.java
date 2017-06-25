package org.kelab.util;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.SneakyThrows;
import org.kelab.util.constant.CharsetConstant;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * Created by hongfei.whf on 2016/11/26.
 */
public class FreeMarkerUtil {

	private final static Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);

	static {
		cfg.setDefaultEncoding(CharsetConstant.UTF_8);
	}

	/**
	 * 渲染模板
	 *
	 * @param id
	 * @param stringTemplate
	 * @param params
	 * @return
	 * @throws IOException
	 * @throws TemplateException
	 */
	@SneakyThrows
	public static String proccessTemplate(String id, String stringTemplate, Object params) {
		Template template = new Template(id, new StringReader(stringTemplate), cfg);
		StringWriter writer = new StringWriter();
		template.process(params, writer);
		return writer.toString();
	}
}
