package org.kelab.vf.generator;

import freemarker.template.TemplateException;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.kelab.util.FileUtil;
import org.kelab.util.FreeMarkerUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by hongfei.whf on 2016/11/26.
 */
@Slf4j
public class MapperGenerator {

	private final static SAXReader reader = new SAXReader();
	private final static String path1 = "auto";
	private final static String path2 = "impl";

	@Setter
	private String output_dir = "E:\\tmp\\output";

	@Autowired
	private MybatisMapping mybatisMapping;

	/**
	 * 开始渲染
	 *
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws TemplateException
	 * @throws DocumentException
	 */
	@SneakyThrows
	public void run() {
		List<Class> classes = mybatisMapping.getBaseEntitys();
		for (Class clazz : classes) {
			System.out.println("=========> 为当前实体类创建mapper：" + clazz.getCanonicalName());
			String namespace = mybatisMapping.getMapperNameSpace(clazz);
			System.out.println("=======> namespace：" + namespace);
			String tableName = mybatisMapping.getTableName(clazz);
			System.out.println("=======> tableName：" + tableName);
			String queryType = mybatisMapping.getQueryType(clazz).getCanonicalName();
			System.out.println("=======> queryType：" + queryType);
			// ResultMapWithBLOBs or BaseResultMap
			String resultMap = getResultMap(clazz);
			System.out.println("=======> resultMap：" + resultMap);
			Map<String, Object> map = new HashMap<>();
			map.put("namespace", namespace);
			map.put("queryType", queryType);
			map.put("tableName", tableName);
			map.put("resultMap", resultMap);

			Class extendsClass = clazz;
			if (mybatisMapping.isInherit(clazz)) {
				// 需要扫描子类
				extendsClass = mybatisMapping.inherit(clazz);
			}

			// base or WithBLOBs
			String ownType = extendsClass.getCanonicalName();
			map.put("ownType", ownType);
			System.out.println("=======> ownType：" + ownType);

			// field
			List<Field> fields = mybatisMapping.classAndSubClassFields(clazz);
			map.put("fields", fields);
			for (Field field : fields) {
				System.out.println("+++++ " + field.getName());
			}

			// field -> column
			Map<String, String> field2Column = mybatisMapping.getField2Column(clazz);
			map.put("field2Column", field2Column);
			System.out.println("=======> field2Column：");
			for (Map.Entry<String, String> entry : field2Column.entrySet()) {
				System.out.println("+++++ " + entry.getKey() + " : " + entry.getValue());
			}

			String template = FileUtil.readResource("mapper-template/TemplateMapper.ftl");
			String custom = FileUtil.readResource("mapper-template/CustomMapper.ftl");
			String TemplateContent = FreeMarkerUtil.proccessTemplate(clazz.getCanonicalName(),
					template, map);
			String auto_render_template_file_path = getRenderPath(clazz, true);
			FileUtils.write(new File(auto_render_template_file_path), TemplateContent);

			String CustomContent = FreeMarkerUtil.proccessTemplate(clazz.getCanonicalName(),
					custom, map);
			String auto_render_custom_file_path = getRenderPath(clazz, false);
			FileUtils.write(new File(auto_render_custom_file_path), CustomContent);
		}
	}

	/**
	 * 获取映射文件
	 * ResultMapWithBLOBs or BaseResultMap
	 *
	 * @param clazz
	 * @return
	 * @throws DocumentException
	 * @throws IOException
	 */
	@SneakyThrows
	private String getResultMap(Class clazz) throws DocumentException, IOException {
		//dom4j的这个验证功能去掉
		reader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
		Document document = reader.read(FileUtil.classpathFile("mybatis-mapper" + File.separator + clazz.getSimpleName() + "Mapper.xml"));
		Element root = document.getRootElement();
		if ("mapper".equals(root.getName())) {
			Iterator iter = root.elementIterator();
			while (iter.hasNext()) {
				Element currElement = (Element) iter.next();
				if ("resultMap".equals(currElement.getName())) {
					if ("ResultMapWithBLOBs".equals(currElement.attributeValue("id"))) {
						return "ResultMapWithBLOBs";
					}
				}
			}
		}
		return "BaseResultMap";
	}

	/**
	 * 渲染输出文件路径
	 *
	 * @param clazz
	 * @param isTemplate
	 * @return
	 */
	private String getRenderPath(Class clazz, Boolean isTemplate) {
		return output_dir +
				File.separator +
				(isTemplate ? path1 : path2) +
				File.separator +
				clazz.getSimpleName() +
				"Mapper.xml";
	}

}
