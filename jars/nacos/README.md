# Nacos 启动运行(单机)

## 配置数据库

1. 安装数据库，版本要求：5.6.5+
2. 新建数据库并且初始化mysql数据库，数据库初始化文件：conf/nacos-mysql.sql
3. 导入nebula的配置文件：conf/nacos-mysql-data.sql
3. 修改conf/application.properties文件，增加支持mysql数据源配置（目前只支持mysql），添加mysql数据源的url、用户名和密码

```bash
spring.datasource.platform=mysql

db.num=1
db.url.0=jdbc:mysql://11.162.196.16:3306/nacos_devtest?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true
db.user=用户名
db.password=密码
```

## 启动命令

```bash
sh startup.sh -m standalone

bash startup.sh -m standalone
```

* 更多部署方式请参考: https://nacos.io/zh-cn/docs/cluster-mode-quick-start.html