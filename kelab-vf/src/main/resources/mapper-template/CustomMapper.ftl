<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="${namespace}">

    <!-- 若要进行修改，请在此补全sql，防止以后模板扩展带来的代码迁移的不便 -->
    <!-- 注意，此两处sql均被使用到，请勿注释或删除 -->
    <!-- where sql -->
    <sql id="condition">
        <where>
            <if test="ids != null">
                <if test="ids.size() > 0">
                ${tableName}.id in
                    <foreach collection="ids" item="item" open="("
                             separator="," close=")">
                    <#noparse>#{item}</#noparse>
                    </foreach>
                </if>
                <if test="ids.size() == 0">
                    1 = 0
                </if>
            </if>
            <if test="id != null">
                and ${tableName}.id = <#noparse>#{id}</#noparse>
            </if>
        </where>
    </sql>
    <!-- orderby sql-->
    <sql id="orderby">

    </sql>
    <!--自定义字段-->
    <sql id="custom_fields">
        <if test="userRoleId == 1">
            <include refid="role_admin_fields"/>
        </if>
        <if test="userRoleId == 2">
            <include refid="role_teacher_fields"/>
        </if>
        <if test="userRoleId == 3">
            <include refid="role_acmer_fields"/>
        </if>
        <if test="userRoleId == 4">
            <include refid="role_student_fields"/>
        </if>
        <if test="userRoleId == 5">
            <include refid="role_no_login_fields"/>
        </if>
    </sql>

    <!--admin-->
    <sql id="role_admin_fields">
        <trim suffixOverrides=",">
        <#list field2Column ? keys as field>
        ${field2Column[field]},
        </#list>
        </trim>
    </sql>
    <!--teacher-->
    <sql id="role_teacher_fields">
        <trim suffixOverrides=",">
        <#list field2Column ? keys as field>
        ${field2Column[field]},
        </#list>
        </trim>
    </sql>
    <!--acmer-->
    <sql id="role_acmer_fields">
        <trim suffixOverrides=",">
        <#list field2Column ? keys as field>
        ${field2Column[field]},
        </#list>
        </trim>
    </sql>
    <!--student-->
    <sql id="role_student_fields">
        <trim suffixOverrides=",">
        <#list field2Column ? keys as field>
        ${field2Column[field]},
        </#list>
        </trim>
    </sql>
    <!--no login-->
    <sql id="role_no_login_fields">
        <trim suffixOverrides=",">
        <#list field2Column ? keys as field>
        ${field2Column[field]},
        </#list>
        </trim>
    </sql>

</mapper>