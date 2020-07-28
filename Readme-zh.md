## 开发工具包
![](https://img.shields.io/badge/%E5%B7%A5%E5%85%B7%E5%8C%85-Tool-orange) 
![](https://img.shields.io/badge/%E6%97%A5%E5%BF%97-Logback-red) 
![](https://img.shields.io/badge/%E8%BF%9B%E5%88%B6%E8%BD%AC%E6%8D%A2-BaseConversion-green) 
![](https://img.shields.io/badge/%E9%9B%AA%E8%8A%B1%E7%AE%97%E6%B3%95-SnowFlake-blue)

[English](https://github.com/GreyCode9/toolkit/blob/master/README.md)

## 快速开始
**maven:**
```xml
<dependency>
    <groupId>top.mjava</groupId>
    <artifactId>toolkit</artifactId>
    <version>0.0.12-RELEASE</version>
</dependency>
```

## 日志脱敏插件
**1. 将`conversionRule`添加到`logback.xml`**
> 注意: 要放在`appender`前面
```xml
<conversionRule conversionWord="msg" converterClass="top.mjava.desensitization.TopLogMsgConvert"/>
```
**2. 在`reources`添加配置文件`logback-desensitization-rule.properties`**

**3. 在`logback-desensitization-rule.properties`里添加脱敏规则**
```properties
# 是否开启脱敏 默认关闭
enable=true
# 日志大小限制  超过直接输出 不进行脱敏 单位 b k m  不填默认 1m  （可选配置）
log_max_length=1m
# 自定义配置脱敏方法类路径 （可选配置）
desensitization_class_path=com.example.logtest.MyDe


# 脱敏字段配置 
# 日志打印格式： 
#   log.info("打印测试：{}",JSON.toString);
# 错误格式：
#   log.info("打印测试"+JSON.toString);
#   log.info(JSON.toString);
# 注意：如果不设置脱敏方法 将采用默认脱敏方法   使用脱敏方法 & 字符后加脱敏方法
#      保留前三位后三位
#     字段长度小于9时  保留后三位
#     字段长度小于6 全脱

# eg:/aa/bb 脱敏aa对象下的bb字段
# eg:/aa/bb/cc 脱敏aa对象里的bb对象的cc字段

# 脱敏类型 MOBILE  EMAIL  BANK_CODE  ID_CARD  PASSWORD  NAME  
# eg:/aa/mobile&MOBILE 脱敏aa对象下的mobile字段  采用MOBILE方法脱敏

```

## 进制转换
> 使用此工具转换为64进制后，您只能使用此工具进行还原

**1. 10进制转64进制:**
```java
String base_64=BaseConvertUtils.base10ToBase64(Long val);
```
**2. 64进制转10进制:**
```java
Long base_10=BaseConvertUtils.base64ToBase10(String val);
```

## 小工具
### Ip工具
- 获取真实ip地址：
```java
String ip=IpUtils.getIpAddress(HttpServletRequest request);
```
