
1、目录与文件清单
 ============================================================================
 本目录			Google Code Hosting顶级目录
  readme.txt	本文件
 
 .settings			Eclipse项目配置
 exam-mts-cvs 	MTS CVS版本库的导出目录
    导入步骤：[beyondCompare]
          config        ->src/main/resource
          webRoot   ->src/main/webapp
          src            -> src/main/java
    
 src					源代码路径
 target               Maven编译输出路径
 .classpath			Eclipse项目文件
 .project			Eclipse项目文件
 pom.xml			Maven配置文件
 
 2、日志
 =============================================================================
 2008/4/24
      cvs 工作目录是有意这样安排，为的是changelog的maven 2 插件能正常工作
      mvn changelog:changelog,会抽取CVS库中的更新日志（changelog.xml=>生成target\site\changelog.html）
      
2008/8/6
	exam-mts-cvs更新操作：cvs update
    
2008-08-15
	从mycop拆解出来；
	mvn install首次编译时StringUtil.java出错，改用UTF-8重保存后哦编译成功 