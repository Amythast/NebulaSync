# JWT 非对称加密

服务端生成公钥和密钥，每个客户端使用获取到的公钥到服务器做认证。

## 1. 生成 jks 证书文件
使用jdk自带的keytool工具算法为RSA：

```bash
keytool -genkeypair -alias [your alias] -keyalg RSA -keypass [your key pass] -keystore [your jks filename] -storepass [your store pass]
```


## 2. 导出公钥

```bash
keytool -list -rfc --keystore [your jks filename] | openssl x509 -inform pem -pubkey
```

## 3.生成公钥文本
使用导出的公钥生成公钥文本: 如 pubkey.txt

## 4. 使用公钥

- 将 pubkey.txt 放到客户端 resources 目录下, 通过公钥解析 jwt token。
- 客户端也可以在启动时，通过访问认证服务的 token_key 资源路径获取公钥，通过公钥解析 jwt token。
