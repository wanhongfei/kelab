<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="${namespace}">

    <!-- query ${tableName} page -->
    <select id="queryPage" resultMap="${resultMap}"
            parameterType="${queryType}">
        select
        <if test="customFields == false">
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
        </if>
        <if test="customFields == true">
            <include refid="custom_fields"/>
        </if>
        from ${tableName}
        <include refid="condition"/>
        <include refid="orderby"/>
    <#noparse>limit #{start},#{rows}</#noparse>
    </select>

    <!-- query ${tableName} list -->
    <select id="queryList" resultMap="${resultMap}"
            parameterType="${queryType}">
        select
        <if test="customFields == false">
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
        </if>
        <if test="customFields == true">
            <include refid="custom_fields"/>
        </if>
        from ${tableName}
        <include refid="condition"/>
        <include refid="orderby"/>
    </select>

    <!-- query ${tableName} by id -->
    <select id="queryById" parameterType="${queryType}" resultMap="${resultMap}">
        select
        <if test="customFields == false">
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
        </if>
        <if test="customFields == true">
            <include refid="custom_fields"/>
        </if>
        from ${tableName}
        where ${tableName}.id = <#noparse>#{id}</#noparse>
    </select>

    <!-- query ${tableName} by ids -->
    <select id="queryByIds" parameterType="${queryType}" resultMap="${resultMap}">
        select
        <if test="customFields == false">
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
        </if>
        <if test="customFields == true">
            <include refid="custom_fields"/>
        </if>
        from ${tableName}
        <where>
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
        </where>
    </select>

    <!-- count ${tableName} -->
    <select id="count" resultType="java.lang.Integer"
            parameterType="${queryType}">
        select
        count(id)
        from ${tableName}
        <include refid="condition"/>
    </select>

    <!--批量更新,data=>实体类，ids=需要更新的主键-->
    <update id="batchDynamicUpdate" parameterType="java.util.Map">
        update ${tableName}
        <set>
        <#list field2Column ? keys as field>
            <#if field != "id">
                <if test="data.${field} != null">
                ${tableName}.${field2Column[field]} = <#noparse>#{</#noparse>data.${field}<#noparse>}</#noparse>
                </if>
            </#if>
        </#list>
        </set>
        <where>
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
        </where>
    </update>

    <!-- delete ${tableName} by ids -->
    <delete id="deleteByIds" parameterType="java.util.List">
        delete from ${tableName}
        <where>
            <if test="list.size() > 0">
            ${tableName}.id in
                <foreach collection="list" item="item" open="("
                         separator="," close=")">
                <#noparse>#{item}</#noparse>
                </foreach>
            </if>
            <if test="list.size() == 0">
                1 = 0
            </if>
        </where>
    </delete>

    <!-- save batch ${tableName} -->
    <insert id="batchSave" parameterType="java.util.List">
        insert into ${tableName}
        <trim prefix="(" suffix=")" suffixOverrides=",">
        <#list field2Column ? keys as field>
        ${field2Column[field]},
        </#list>
        </trim>
        value
        <foreach item="item" index="index" collection="list" separator=",">
            <trim prefix="(" suffix=")" suffixOverrides=",">
            <#list field2Column ? keys as field>
                <#noparse>#{item.</#noparse>${field}<#noparse>},</#noparse>
            </#list>
            </trim>
        </foreach>
    </insert>

    <!-- save ${tableName} And Return Id -->
    <insert id="saveAndRetId" parameterType="${ownType}"
            useGeneratedKeys="true" keyProperty="id">
        insert into ${tableName}
        <trim prefix="(" suffix=")" suffixOverrides=",">
        <#list field2Column ? keys as field>
            <if test="${field} != null">
            ${field2Column[field]},
            </if>
        </#list>
        </trim>
        <trim prefix="values (" suffix=")" suffixOverrides=",">
        <#list field2Column ? keys as field>
            <if test="${field} != null">
                <#noparse>#{</#noparse>${field}<#noparse>},</#noparse>
            </if>
        </#list>
        </trim>
    </insert>

</mapper>