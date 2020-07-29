package top.mjava.base;

import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * 进制转换工具
 *
 * @author zmh
 * @version 1.0
 * @date 2020/7/27
 **/
public class BaseConvertUtils {
    private final static int BASE_16_POWER=4;
    private final static int BASE_32_POWER=5;
    private final static int BASE_64_POWER=6;



    /**
     * 10进制转16进制
     * */
    public static String base10ToHex(Long val){
        return base10ToAll(val,BaseDictionary.HEX_ARRAY.getChars(),BASE_16_POWER);
    }

    /**
     * 16进制转10进制
     * */
    public static Long hexToBase10(String val){
        return allToBase10(val,BaseDictionary.HEX_ARRAY.getChars(),BASE_16_POWER);
    }

    /**
     * 10进制转32进制
     * */
    public static String base10ToBase32(Long val){
        return base10ToAll(val,BaseDictionary.BASE_32_ARRAY.getChars(),BASE_32_POWER);
    }

    /**
     * 32进制转10进制
     * */
    public static Long base32ToBase10(String val){
        return allToBase10(val,BaseDictionary.BASE_32_ARRAY.getChars(),BASE_32_POWER);
    }

    /**
     * 10进制转64进制
     * */
    public static String base10ToBase64(Long val){
        return base10ToAll(val,BaseDictionary.BASE_64_URL_ARRAY.getChars(),BASE_64_POWER);
    }

    /**
     * 64进制转10进制
     * */
    public static Long base64ToBase10(String val){
        return allToBase10(val,BaseDictionary.BASE_64_URL_ARRAY.getChars(),BASE_64_POWER);
    }

    private static Long allToBase10(String val,char[] chars,int power){
        long result=0L;
        char[] c=val.toCharArray();
        Map<Character,Integer> map=new HashMap<>();
        for (int i=0;i<chars.length;i++){
            map.put(chars[i],i);
        }
        for (int j=0;j<c.length;j++){
            // eg: 64转10进制 从右到左 从64的0次方开始
            // eg: (eq)64 -> (e*64^1+q*64^0)10
            // 然后根据字典位置 e在字典下标为2 q在字典下标为0 -> (2*64^1+0*64^0)10=128
            result += (long) map.get(c[j])<<(power*(c.length-j-1));
        }
        return result;
    }

    private static String base10ToAll(Long val,char[] chars,int power){
        Long l1=val;
        long y;
        StringBuilder result=new StringBuilder();
        // 这里用到了linkedList的先进后出
        // eq:  in:abc  out:cba
        LinkedList<Character> list=new LinkedList<>();
        while (l1 > 0){
            //取余数运算
            y=l1-((l1>>power)<<power);
            list.push(chars[(int) y]);
            l1=l1>>power;
        }
        //从栈头取出数据
        while (!list.isEmpty()){
            result.append(list.pop());
        }
        return result.toString();
    }

    @Test
    public void test(){
        System.out.println(base10ToBase64(6644422222L));
        System.out.println(base64ToBase10(base10ToBase64(6644422222L)));

        System.out.println(base10ToBase32(123123123445567L));
        System.out.println(base32ToBase10(base10ToBase32(123123123445567L)));

        System.out.println(base10ToHex(9898989887L));
        System.out.println(hexToBase10(base10ToHex(9898989887L)));
    }
}
