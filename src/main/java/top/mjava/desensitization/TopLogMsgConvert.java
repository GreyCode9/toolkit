package top.mjava.desensitization;

import ch.qos.logback.classic.pattern.MessageConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.util.ResourceUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;


/**
 * @Author zmh
 * @Date 2020/7/7
 * @Version 1.0
 **/
public class TopLogMsgConvert extends MessageConverter {
    private Map<String, LinkedList<String>> relusMap;
    private boolean init = false;
    private boolean enable=false;
    private Desensitization desensitization=new Desensitization();
    private boolean fileIOException =false;
    private int maxlength =1048576;
    private static final String CONFIG_PATH="classpath:logback-desensitization-rule.properties";
    private static final String MAP_KEY_DELIMITER ="#";
    private static final String PROPERTIES_KEY_DELIMITER ="/";
    private static final String PROPERTIES_KEY_VALUE_DELIMITER ="&";
    private static final String PROPERTIES_KEY_ENABLE="enable";
    private static final String PROPERTIES_KEY_ENABLE_TRUE="true";
    private static final String PROPERTIES_KEY_LOG_MAX_LENGTH="log_max_length";
    private static final String PROPERTIES_KEY_DESENSITIZATION_CLASS_PATH="desensitization_class_path";

    @Override
    public String convert(ILoggingEvent event){
        if (!this.init){
            this.init();
        }
        if (this.relusMap!=null && this.enable && !this.fileIOException ){
            if (event.getFormattedMessage().getBytes().length> maxlength){
                //超过设定长度 不脱敏 直接返回
                return super.convert(event);
            }
            //开启脱敏
            return this.convertMsg(event);
        }
        return super.convert(event);
    }

    /**
     * 初始化配置
     * */
    private void init() {
        synchronized (TopLogMsgConvert.class) {
            this.relusMap = new HashMap<>();
            try {
                File file=ResourceUtils.getFile(CONFIG_PATH);
                InputStream in = new BufferedInputStream(new FileInputStream(file));
                Properties p = new Properties();
                p.load(in);
                int keyNum=0;
                for (Object k:p.keySet()){
                    keyNum++;
                    if (PROPERTIES_KEY_ENABLE.equals(k)){
                        if (PROPERTIES_KEY_ENABLE_TRUE.equals(p.getProperty(k.toString()))){
                            this.enable=true;
                        }
                    }else if(PROPERTIES_KEY_LOG_MAX_LENGTH.equals(k)){
                        if (!p.getProperty(k.toString()).isEmpty()){
                            int length=this.getByteLength(p.getProperty(k.toString()));
                            if (length>0){
                                this.maxlength=length;
                            }
                        }
                    }else if (PROPERTIES_KEY_DESENSITIZATION_CLASS_PATH.equals(k)){
                        if (!p.getProperty(k.toString()).isEmpty()){
                            try {
                                Class<?> c=Class.forName(p.getProperty(k.toString()));
                                this.desensitization= (Desensitization) c.newInstance();
                            }catch (Exception e){
                                System.out.println("\033[31;4m" +e.getMessage()+"\n" +e+ "\033[0m");
                            }
                        }
                    }else{
                        String key=k.toString();
                        String[] kv=key.split(PROPERTIES_KEY_VALUE_DELIMITER);
                        if (kv.length==2){
                            String k2=keyNum+MAP_KEY_DELIMITER+kv[1];
                            String[] ks=kv[0].split(PROPERTIES_KEY_DELIMITER);
                            LinkedList<String> linkedList=new LinkedList<>();
                            for (String value:ks){
                                if (!value.isEmpty()){
                                    linkedList.addLast(value);
                                }
                            }
                            this.relusMap.put(k2,linkedList);
                        }else if (kv.length==1){
                            String k2=keyNum+MAP_KEY_DELIMITER;
                            String[] ks=kv[0].split(PROPERTIES_KEY_DELIMITER);
                            LinkedList<String> linkedList=new LinkedList<>();
                            for (String value:ks){
                                if (!value.isEmpty()){
                                    linkedList.addLast(value);
                                }
                            }
                            this.relusMap.put(k2,linkedList);
                        }
                    }
                }
                this.init = true;
            }catch (Exception e){
                System.out.println("\033[31;4m" + "Abnormal loading desensitization configuration file：\n" +e+ "\033[0m");
                this.fileIOException =true;
                this.init = true;
            }
        }
    }

    private String convertMsg(ILoggingEvent event){
        /**
         * 获取mag
         * eg：log.info("测试数据：{}"，arg)
         * 获取“测试数据：{}”
         * */
        String msg=event.getMessage();

        /**
         * 遍历所有参数
         * eg：log.info("测试数据：{}",arg)
         * 可以获取到arg数据
         * */
        Object[] args=event.getArgumentArray();
        if (args!=null && args.length>0){
            for (int i=0;i<args.length;i++){
                String jsonString=args[i].toString();
                if ("{".equals(jsonString.substring(0,1)) &&
                        "}".equals(jsonString.substring(jsonString.length()-1))){
                    try {
                        JSONObject arg= JSON.parseObject(jsonString);
                        LinkedList<String> list=null;
                        for (String k:relusMap.keySet()){
                            String[] k2=k.split(MAP_KEY_DELIMITER);
                            list=new LinkedList<>(relusMap.get(k));
                            if (k2.length==1){
                                this.jsonDesensitization(arg,list,"default");
                            }else if (k2.length==2){
                                this.jsonDesensitization(arg,list,k2[1]);
                            }
                        }
                        args[i]=arg;
                    }catch (JSONException e){
                        System.out.println("\033[31;4m" + e.getMessage()+"\n" +e+ "\033[0m");
                    }
                }
            }
        }
        return MessageFormatter.arrayFormat(msg, args).getMessage();
    }

    /**
     * json层级循环脱敏
     * @param json json对象
     * @param list 层级列表
     * @param type 脱敏类型
     * */
    private  void jsonDesensitization(JSONObject json, LinkedList<String> list,String type) {
        if (json.get(list.getFirst()) instanceof JSONObject){
            JSONObject jsonObject= (JSONObject) json.get(list.getFirst());
            list.removeFirst();
            if (list.size()>0){
                this.jsonDesensitization(jsonObject,list,type);
            }
        }else if (json.get(list.getFirst()) instanceof String){
            String v=json.get(list.getFirst()).toString();
            v=desensitization.type(type,v);
            json.put(list.getFirst(),v);
        }
    }

    /**
     * 字节换算
     * 8bit(位)=1Byte(字节)
     * 1024Byte(字节)=1KB
     * 1024KB=1MB
     * 1024MB=1GB
     * 1024GB=1TB
     * */
    private int getByteLength(String l){
        if (l==null || l.isEmpty()){
            return -1;
        }
        int res=-1;
        try {
            res=Integer.parseInt(l.substring(0,l.length()-1));
        }catch (Exception e){
            System.out.println("\033[31;4m" + "Type conversion error：\n" +e+ "\033[0m");
            return -1;
        }
        switch (l.substring(l.length()-1)){
            case "b":
                break;
            case "k":
                res=res*1024;
                break;
            case "m":
                res=res*1024*1024;
                break;
            default:
                break;
        }
        return res;
    }

}
