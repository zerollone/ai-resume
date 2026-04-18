//package com.ai.resume;
//
//import com.baomidou.mybatisplus.annotation.DbType;
//import com.baomidou.mybatisplus.generator.AutoGenerator;
//import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
//import com.baomidou.mybatisplus.generator.config.GlobalConfig;
//import com.baomidou.mybatisplus.generator.config.PackageConfig;
//import com.baomidou.mybatisplus.generator.config.StrategyConfig;
//import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
//
///**
// * mybatis-plus 代码自动生成器
// */
//public class codeGeneration {
//    public static void main(String[] args){
//        // 创建generator对象
//        AutoGenerator autoGenerator = new AutoGenerator();
//
//        /**
//         * 设置数据源，数据库的相关配置
//         */
//        DataSourceConfig dataSourceConfig = new DataSourceConfig();
//        dataSourceConfig.setDbType(DbType.MYSQL);
//        dataSourceConfig.setDriverName("com.mysql.cj.jdbc.Driver");
//        dataSourceConfig.setUsername("root");
//        dataSourceConfig.setPassword("ai@resume0415");
//        dataSourceConfig.setUrl("jdbc:mysql://101.35.183.75:3306/ai_resume?useUnicode=true&characterEncoding=UTF-8");
//        autoGenerator.setDataSource(dataSourceConfig);
//
//        /**
//         * 全局配置
//         */
//        GlobalConfig globalConfig = new GlobalConfig();
//        // 打开输出目录
//        globalConfig.setOpen(false);
//        // 生成的文件输出的目录
//        globalConfig.setOutputDir(System.getProperty("user.dir")+"/src/main/java");
//        // 设置作者名
//        globalConfig.setAuthor("ws");
//        // xml 开启 BaseResultMap
//        globalConfig.setBaseResultMap(true);
//        // xml 开启 BaseColumnList
//        globalConfig.setBaseColumnList(true);
//        // 实体属性 Swagger2 注解
//        globalConfig.setSwagger2(true);
//        globalConfig.setServiceName("%sService");
//        autoGenerator.setGlobalConfig(globalConfig);
//
//        /**
//         * 包信息
//         */
//        PackageConfig packageConfig = new PackageConfig();
//        packageConfig.setParent("com.ai.resume");
//        packageConfig.setEntity("entity");
//        packageConfig.setMapper("mapper");
//        packageConfig.setController("controller");
//        packageConfig.setService("service");
//        packageConfig.setServiceImpl("service.impl");
//        autoGenerator.setPackageInfo(packageConfig);
//
//        /**
//         * 配置策略
//         */
//        StrategyConfig strategyConfig = new StrategyConfig();
//        // lombok 模型
//        strategyConfig.setEntityLombokModel(true);
//        // 数据库表名映射到实体的命名策略，驼峰命名
//        strategyConfig.setNaming(NamingStrategy.underline_to_camel);
//        // 数据库字段名映射到实体的命名策略
//        strategyConfig.setColumnNaming(NamingStrategy.underline_to_camel);
//        // 生成 @RestController 控制器
//        strategyConfig.setRestControllerStyle(true);
//        // 表前缀，忽略指定的表的前缀
////        strategyConfig.setTablePrefix("t_");
//        autoGenerator.setStrategy(strategyConfig);
//        autoGenerator.execute();
//    }
//}