package cn.org.rapid_framework.generator.util.typemapping;

import cn.org.rapid_framework.generator.util.StringHelper;

public class JavaTypeUtils {

    public static boolean isNeedImport(String type) {
        if (StringHelper.isBlank(type)) {
            return false;
        }

        if (type.startsWith("java.lang.")) {
            return false;
        }

        if ((type.indexOf(".") < 0) && Character.isLowerCase(type.charAt(0))) {
            return false;
        }

        return true;
    }
    
}
