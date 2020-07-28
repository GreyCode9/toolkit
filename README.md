# Development kit
![](https://img.shields.io/badge/%E5%B7%A5%E5%85%B7%E5%8C%85-Tool-orange) 
![](https://img.shields.io/badge/%E6%97%A5%E5%BF%97-Logback-red) 
![](https://img.shields.io/badge/%E8%BF%9B%E5%88%B6%E8%BD%AC%E6%8D%A2-BaseConversion-green) 
![](https://img.shields.io/badge/%E9%9B%AA%E8%8A%B1%E7%AE%97%E6%B3%95-SnowFlake-blue)

[中文文档](https://github.com/GreyCode9/toolkit/blob/master/Readme-zh.md)
## Quick start
**maven:**
```xml
<dependency>
    <groupId>top.mjava</groupId>
    <artifactId>toolkit</artifactId>
    <version>0.0.12-RELEASE</version>
</dependency>
```

## Log masking plugin
**1. Add conversionRule to `logback.xml`**
> Note: put in front of appender
```xml
<conversionRule conversionWord="msg" converterClass="top.mjava.desensitization.TopLogMsgConvert"/>
```
**2. create `logback-desensitization-rule.properties` in resource of your project**

**3. add rule in `logback-desensitization-rule.properties`**
```properties
# Whether to turn on desensitization, default off
enable=true

# Log size limit exceeds direct output, 
# no desensitization unit b k m If not filled, 
# the default is 1m (optional configuration)
log_max_length=1m

# Custom configuration desensitization method classpath (optional configuration)
desensitization_class_path=com.example.logtest.MyDe


# Masking field configuration
# Log print format： 
#   log.info("test：{}",JSON.toString);
# Wrong format：
#   log.info("test"+JSON.toString);
#   log.info(JSON.toString);

# Note: If no desensitization method is set, the default desensitization method will be used
# Use desensitization method: `&` after the desensitization method
# Keep the first three and the last three
# When the field length is less than 9, the last three digits are reserved
# Field length is less than 6 full off

# eg:/aa/bb     `bb` field under desensitized `aa` object
# eg:/aa/bb/cc  The `cc` field of the `bb` object in the desensitized `aa` object

# Type of desensitization: MOBILE  EMAIL  BANK_CODE  ID_CARD  PASSWORD  NAME  
# eg:/aa/mobile&MOBILE    The `mobile` field under the desensitization `aa` object is desensitized using the MOBILE method        

```
## Base Conversion
**1. Base-10 to Base-64:**
```java
String base_64=BaseConvertUtils.base10ToBase64(Long val);
```

**2. Base-64 to Base-10:**
```java
Long base_10=BaseConvertUtils.base64ToBase10(String val);
```
**3. Base-10 to Base-32 (removal: I, L, O, U)：**
```java
String base_32=BaseConvertUtils.base10ToBase32(Long val)
```
**4. Base-32 to Base-10 (remove: I, L, O, U):**
```java
Long base_10=BaseConvertUtils.base32ToBase10(String val);
```
**5. Base-10 to hexadecimal：**
```java
String base_16=BaseConvertUtils.base10ToHex(Long val);
```
**6. Hexadecimal to Base-10：**
```java
Long base_10=BaseConvertUtils.hexToBase10(String val)
```
## Tools
### Ip Tool
- Get real ip address：
```java
String ip=IpUtils.getIpAddress(HttpServletRequest request);
```