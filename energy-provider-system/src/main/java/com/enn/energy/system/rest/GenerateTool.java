package com.enn.energy.system.rest;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.DbType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.enn.constant.StatusCode;
import com.enn.energy.system.common.util.RequestUtils;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.GenerateReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自动生成代码
 *
 * @author kongweiteng
 */
@RestController
@RequestMapping("/generateTool")
@Api(value = "工具", tags ={"代码生成"} )
public class GenerateTool {
    @Value("${spring.datasource.driverClassName}")
    private String driverClassName;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    /*public static void main(String[] args) {
        GenerateReq generateReq = new GenerateReq();
        generateReq.adders="F:/Git/etsp_cloud/energy-provider-system";
        generateReq.author="kongweiteng";
        generateReq.secret="etspSecret";
        generateReq.table="meter_reading_hour";
        GenerateResp generate = generate(generateReq);

        System.err.println("--------------"+generate.msg+"-----------------");
    }*/
    @ApiOperation(value = "代码生成", notes = "代码生成")
    @RequestMapping(value = "/generate",method = RequestMethod.POST)
    public EnergyResp<String> generate(@RequestBody GenerateReq generateReq,HttpServletRequest request) {
        //定义返回结果
        EnergyResp<String> generateResp = new EnergyResp<String>();
        //  ---------------检查参数------开始---------
        if (generateReq.getAdders() != null && generateReq.getAuthor() != null && generateReq.getSecret() != null && generateReq.getTable() != null) {
            //检查秘钥
            if (!generateReq.getSecret().equals("066131f0a7834afea8b211046af67749")) {
                generateResp.faile(StatusCode.ERROR.getCode(),"秘钥错误，请联系管理员");
                return generateResp;
            }
            if (generateReq.getAuthor().equals("")) {
                generateResp.faile(StatusCode.ERROR.getCode(),"开发人员名称，请写自己的名字，谢谢！");
                return generateResp;
            }
        } else {
            generateResp.faile(StatusCode.ERROR.getCode(),"请把参数填写完整");
            return generateResp;
        }
        //获取请求地址--只能是localhost时才能生成代码
        String basePath = RequestUtils.basePath(request);
        if(! basePath.contains("localhost")){
            generateResp.faile(StatusCode.ERROR.getCode(),"只能在开发本地生成代码，请使用localhost访问应用！");
            return generateResp;
        }

        //  ---------------检查参数------结束---------


        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(generateReq.getAdders() + "/energy-provider-system/src/main/java");
        gc.setFileOverride(true);//是否覆盖已有文件
        gc.setActiveRecord(true);//开启 ActiveRecord 模式
        gc.setEnableCache(false);// XML 二级缓存
        gc.setBaseResultMap(true);// XML ResultMap
        gc.setBaseColumnList(false);// XML columList
        gc.setAuthor(generateReq.getAuthor());//开发人员

        // 自定义文件命名，注意 %s 会自动填充表实体属性！
        // gc.setMapperName("%sDao");
        // gc.setXmlName("%sDao");
        // gc.setServiceName("MP%sService");
        // gc.setServiceImplName("%sServiceDiy");
        // gc.setControllerName("%sAction");
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL);
        dsc.setTypeConvert(new MySqlTypeConvert() {
            // 自定义数据库表字段类型转换【可选】
            @Override
            public DbColumnType processTypeConvert(String fieldType) {
                System.out.println("转换类型：" + fieldType);
                // 注意！！processTypeConvert 存在默认类型转换，如果不是你要的效果请自定义返回、非如下直接返回。
                return super.processTypeConvert(fieldType);
            }
        });
        dsc.setDriverName(driverClassName);
        dsc.setUsername(username);
        dsc.setPassword(password);
        dsc.setUrl(url);
        mpg.setDataSource(dsc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        // strategy.setCapitalMode(true);// 全局大写命名 ORACLE 注意
        //strategy.setTablePrefix(new String[] { "tlog_", "tsys_" });// 此处可以修改为您的表前缀
        strategy.setNaming(NamingStrategy.underline_to_camel);// 表名生成策略
        strategy.setInclude(new String[]{generateReq.getTable()}); // 需要生成的表
        // strategy.setExclude(new String[]{"test"}); //
        // 自定义实体父类
        //strategy.setSuperEntityClass("com.etrade.energy.common.base.BaseEntity");
        // 自定义实体，公共字段
        // strategy.setSuperEntityColumns(new String[] { "test_id", "age" });
        // 自定义 mapper 父类
        // strategy.setSuperMapperClass("com.baomidou.authentication.TestMapper");
        // 自定义 service 父类
        //strategy.setSuperServiceClass("com.etrade.energy.common.base.BaseService");
        // 自定义 service 实现类父类
        //strategy.setSuperServiceImplClass("com.etrade.energy.common.base.BaseService");
        // 自定义 controller 父类
        //strategy.setSuperControllerClass("com.etrade.energy.common.base.BaseController");
        // 【实体】是否生成字段常量（默认 false）
        // public static final String ID = "test_id";
        // strategy.setEntityColumnConstant(true);
        // 【实体】是否为构建者模型（默认 false）
        // public User setName(String name) {this.name = name; return this;}
        // strategy.setEntityBuilderModel(true);
        mpg.setStrategy(strategy);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent("com.enn.energy.system");
        pc.setController("rest");
        pc.setEntity("entity");
        pc.setMapper("dao");
        //pc.setModuleName("test");
        mpg.setPackageInfo(pc);

        // 注入自定义配置，可以在 VM 中使用 cfg.abc 【可无】
        InjectionConfig cfg = new InjectionConfig() {
            @Override

            public void initMap() {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("abc", this.getConfig().getGlobalConfig().getAuthor() + "-Mp");
                this.setMap(map);
            }
        };

        //输出文件配置
        List<FileOutConfig> focList = new ArrayList<FileOutConfig>();
        /*
        // 自定义 xxList.jsp 生成
        focList.add(new FileOutConfig("/template/list.jsp.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输入文件名称
                return "D://my_" + tableInfo.getEntityName() + ".jsp";
            }
        });
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);
        */

        // 调整 xml 生成目录演示
        focList.add(new FileOutConfig("/templates/mapper.xml.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return generateReq.getAdders() + "/energy-provider-system/src/main/resources/mybatis/mapper/" + tableInfo.getEntityName() + "Mapper.xml";
            }
        });
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        // 关闭默认 xml 生成，调整生成 至 根目录
        TemplateConfig tc = new TemplateConfig();
        tc.setXml(null);
        mpg.setTemplate(tc);

        // 自定义模板配置，可以 copy 源码 mybatis-plus/src/main/resources/template 下面内容修改，
        // 放置自己项目的 src/main/resources/template 目录下, 默认名称一下可以不配置，也可以自定义模板名称
        // TemplateConfig tc = new TemplateConfig();
        // tc.setController("...");
        //tc.setEntity("...");
        // tc.setMapper("...");
        // tc.setXml("...");
        // tc.setService("...");
        // tc.setServiceImpl("...");
        // 如上任何一个模块如果设置 空 OR Null 将不生成该模块。
        // mpg.setTemplate(tc);

        // 执行生成
        mpg.execute();

        // 打印注入设置【可无】
        System.err.println(mpg.getCfg().getMap().get("abc"));
        generateResp.ok("代码生成成功");
        return generateResp;
    }

}
