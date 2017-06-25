<h2>1. 项目描述：</h2>
Kelab vf框架代码<br/>
<br/>
<h2>2. 项目脚本：</h2>
2.1 执行/bin/run.bat/run.sh，使用Maven内置Tomcat发布项目<br/>
2.2 执行/bin/deploy.bat/package.sh，打包所有模块<br/>
2.3 执行/bin/analyze.bat/analyze.sh，打印Tree.txt文件分析jar冲突<br/>
<br/>
<h2>3. Maven 配置文件示例:</h2>
```
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
		xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">
	<localRepository>E:\repo\maven</localRepository> 
	<pluginGroups></pluginGroups>
	<proxies></proxies>
	<servers>
		<server> 
			<id>tomcat7</id> 
			<username>admin</username> 
			<password>admin</password> 
		</server> 
		<server>
			<id>kelab-releases</id>
			<username>kelab</username>
			<password>cs.swust</password>
		</server>
		<server>
			<id>kelab-snapshots</id>
			<username>kelab</username>
			<password>cs.swust</password>
		</server> 
	</servers>
	<mirrors>
		<mirror>
			<id>aliyun</id>
			<url>http://maven.aliyun.com/nexus/content/groups/public/</url>
			<mirrorOf>central</mirrorOf>
		</mirror>
	</mirrors>
	<profiles>
		<profile>
			<activation>
				<activeByDefault>true</activeByDefault>
			</activation>
			<repositories>
				<repository>
					<id>kelab</id>
					<url>http://192.168.0.27:8081/nexus/content/repositories/kelab-releases/</url>
				</repository>
				<releases>
					<enabled>true</enabled>
					<updatePolicy>always</updatePolicy>
					<checksumPolicy>warn</checksumPolicy>
				</releases>
				<snapshots>
					<enabled>true</enabled>
					<updatePolicy>always</updatePolicy>
					<checksumPolicy>warn</checksumPolicy>
				</snapshots>
			</repositories>
			<pluginRepositories>
				<pluginRepository>
					<id>kelab</id>
					<url>http://192.168.0.27:8081/nexus/content/repositories/kelab-releases/</url>
				</pluginRepository>
				<releases>
					<enabled>true</enabled>
					<updatePolicy>always</updatePolicy>
					<checksumPolicy>warn</checksumPolicy>
				</releases>
				<snapshots>
					<enabled>true</enabled>
					<updatePolicy>always</updatePolicy>
					<checksumPolicy>warn</checksumPolicy>
				</snapshots>
			</pluginRepositories>
		</profile>
	</profiles> 
</settings>

```