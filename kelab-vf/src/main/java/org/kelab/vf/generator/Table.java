package org.kelab.vf.generator;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by hongfei.whf on 2016/11/27.
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Table {

	/**
	 * 实体类对应的table名
	 *
	 * @return
	 */
	String name();
}
