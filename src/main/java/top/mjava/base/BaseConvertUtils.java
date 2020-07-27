package top.mjava.base;

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
    private static final char[] BASE_64_ARRAY = { 'z','x','c','v','b','n','m','a','s','d','f','g','h','j','k','l','q','w',
    'e','r','t','y','u','i','o','p','1','2','3','4','5','6','-','7','8','9','Z','X','C','V','B','N','M','A','S','D','F',
    'G','H','J','K','L','Q','W','E','R','T','Y','U','I','O','P','0','+'};

    /**
     * 10进制转64进制
     * */
    public static String base10ToBase64(Long val){
        Long l1=val;
        long y;
        StringBuilder result=new StringBuilder();
        // 这里用到了linkedList的先进后出
        // eq:  in:abc  out:cba
        LinkedList<Character> list=new LinkedList<>();
        while (l1 > 0){
            //取余数运算
            y=l1-((l1>>6)<<6);
            list.push(BASE_64_ARRAY[(int) y]);
            l1=l1>>6;
        }
        //从栈头取出数据
        while (!list.isEmpty()){
            result.append(list.pop());
        }
        return result.toString();
    }

    /**
     * 64进制转10进制
     * */
    public static Long base64ToBase10(String val){
        char[] c=val.toCharArray();
        long result=0;
        // 将字典哈希
        Map<Character,Integer> map=new HashMap<>();
        for (int i=0;i<BASE_64_ARRAY.length;i++){
            map.put(BASE_64_ARRAY[i],i);
        }
        for (int i=0;i<c.length;i++){
            // 64转10进制 从右到左 从64的0次方开始
            // eq: (eq)64 -> (e*64^1+q*64^0)10
            // 然后根据字典位置 e在字典下标为2 q在字典下标为0 -> (2*64^1+0*64^0)10=128
            result+=map.get(c[i])<<(6*(c.length-1-i));
        }
        return result;
    }
}
