package org.kelab.test.util;

import org.kelab.vf.junit.JunitBaseServiceDao;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 * Created by wanhongfei on 2016/12/21.
 */
@ContextConfiguration(locations = {"classpath*:dispatcher-servlet.xml"})
@TransactionConfiguration(defaultRollback = true)
public class JdbcUtilTest extends JunitBaseServiceDao {

}
