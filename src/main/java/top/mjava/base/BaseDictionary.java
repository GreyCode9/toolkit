package top.mjava.base;

/**
 * 进制转换字典
 * @author zmh
 * @version 1.0
 * @date 2020/7/29
 **/
public enum BaseDictionary {
    BASE_2_ARRAY("2进制","2进制字典",new char[]{'0','1'}),

    BASE_64_ARRAY("64进制","64进制乱序字典",new char[]{'z','x','c','v','b','n','m','a','s','d','f','g','h','j','k','l','q','w',
            'e','r','t','y','u','i','o','p','1','2','3','4','5','6','-','7','8','9','Z','X','C','V','B','N','M','A','S','D','F',
            'G','H','J','K','L','Q','W','E','R','T','Y','U','I','O','P','0','+'}),
    BASE_64_URL_ARRAY("64进制","64进制url标准字典",new char[]{'A','B','C','D','E','F','G','H','I','J','K','L','M','N',
            'O','P','Q','R','S','T','U','V','W','X','Y','Z','a','b','c','d','e','f','g','h','i','j','k','l','m','n',
            'o','p','q','r','s','t','u','v','w','x','y','z','0','1','2','3','4','5','6','7','8','9','-','_'}),
    HEX_ARRAY("16进制","16进制标准字典",new char[]{'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'}),
    BASE_32_ARRAY("32进制","32进制标准字典",new char[]{'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E'
            ,'F','G','H','J','K','M','N','P','Q','R','S','T','V','W','X','Y','Z'});

    private String name;
    private String desc;
    private char[] chars;

    BaseDictionary(String name, String desc, char[] chars) {
        this.name = name;
        this.desc = desc;
        this.chars = chars;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public char[] getChars() {
        return chars;
    }

    public void setChars(char[] chars) {
        this.chars = chars;
    }
}
