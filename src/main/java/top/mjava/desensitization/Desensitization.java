package top.mjava.desensitization;

/**
 * @Author zmh
 * @Date 2020/7/7
 * @Version 1.0
 **/
public class Desensitization {

    public String type(String type,String value){
        if (value!=null && !value.isEmpty()){
            String res="";
            switch (type){
                case "MOBILE":
                    res=mobileDesensitization(value);
                    break;
                case "EMAIL":
                    res=emailDesensitization(value);
                    break;
                case "BANK_CODE":
                    res=bakCodeDesensitization(value);
                    break;
                case "ID_CARD":
                    res=idCardDesensitization(value);
                    break;
                case "PASSWORD":
                    res=pwdDesensitization(value);
                    break;
                case "NAME":
                    res=nameDesensitization(value);
                    break;
                default:
                    res=desensitization(value);
                    break;
            }
            return res;
        }else{
            return value;
        }
    }

    /**
     * 通用脱敏  保留前三位后三位
     * 字段长度小于9时  保留后三位
     * 字段长度小于6 全脱
     **/
    public String desensitization(String s){
        if (s==null || s.isEmpty()){
            return s;
        }
        if (s.length()<9){
            if (s.length()<6){
                return "******";
            }
            return "******"+s.substring(6);
        }
        return s.substring(0,3)+"******"+s.substring(s.length()-3);
    }


    /**
     * 中国 手机号脱敏
     * 保留前三位 后四位
     * */
    public String mobileDesensitization(String mobile){
        if (mobile==null || mobile.isEmpty()){
            return mobile;
        }
        if (mobile.length()!=11){
            return mobile;
        }
        return mobile.substring(0,3)+"****"+mobile.substring(mobile.length()-4);
    }

    /**
     * 密码脱敏
     * 全脱
     * */
    public String pwdDesensitization(String pwd){
        if (pwd==null || pwd.isEmpty()){
            return pwd;
        }
        return "********";
    }

    /**
     * 身份证脱敏  18位
     * 保留最后4位
     * */
    public String idCardDesensitization(String idCard){
        if (idCard==null || idCard.isEmpty()){
            return idCard;
        }
        return "**************"+idCard.substring(idCard.length()-4);
    }

    /**
     * 邮箱脱敏
     * 保留 @后面的字符
     * */
    public String emailDesensitization(String email){
        if (email==null || email.isEmpty()){
            return email;
        }
        String[] e=email.split("@");
        if (e.length!=2){
            return email;
        }
        if (e[0]==null || e[0].isEmpty()){
            return email;
        }
        String pre=this.desensitization(e[0]);
        return pre+"@"+e[1];
    }

    /**
     * 银行卡脱敏
     * 保留前6位 后4位
     * */
    public String bakCodeDesensitization(String idCard){
        if (idCard==null || idCard.isEmpty()){
            return idCard;
        }
        return replaceBetween(idCard,6,idCard.length()-4,"*");
    }

    /**
     * 名字脱敏
     * 去除姓氏 保留名
     * */
    public String nameDesensitization(String name){
        if (name==null || name.isEmpty()){
            return name;
        }
        return "*"+name.substring(1);
    }

    /**
     * 将字符串开始位置到结束位置之间的字符用指定字符替换
     * @param sourceStr 待处理字符串
     * @param begin	开始位置
     * @param end	结束位置
     * @param replacement 替换字符
     * @return
     */
    private static String replaceBetween(String sourceStr, int begin, int end, String replacement) {
        if (sourceStr == null) {
            return "";
        }
        if (replacement == null) {
            replacement = "*";
        }
        int replaceLength = end - begin;
        if (sourceStr.isEmpty() && replaceLength > 0) {
            StringBuilder sb = new StringBuilder(sourceStr);
            sb.replace(begin, end, repeat(replacement,replaceLength));
            return sb.toString();
        } else {
            return sourceStr;
        }
    }


    private static String repeat(String s,int count){
        StringBuilder sb=new StringBuilder();
        for (int i=0;i<=count;i++){
            sb.append(s);
        }
        return sb.toString();
    }
}
