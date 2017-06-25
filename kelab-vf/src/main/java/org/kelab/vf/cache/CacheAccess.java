package org.kelab.vf.cache;

import java.lang.annotation.*;

/**
 * @author wwhhf
 * @comment 表示该方法需要先进行缓存查询，若命中则直接返回，否则执行该方法
 * @since 2016年6月1日
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
@Inherited
public @interface CacheAccess {

}