package org.kelab.vf.cache;

import java.lang.annotation.*;

/**
 * @author wwhhf
 * @comment 表示该dao方法需要先进行缓存清除，再执行该方法
 * @since 2016年6月1日
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
@Inherited
public @interface CacheFlush {
}