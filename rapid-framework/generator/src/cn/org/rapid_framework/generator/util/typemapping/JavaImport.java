package cn.org.rapid_framework.generator.util.typemapping;

import java.util.TreeSet;

public class JavaImport {
    TreeSet<String> imports = new TreeSet<String>();

    public void addImport(String javaType) {
        if (JavaTypeUtils.isNeedImport(javaType)) {
            imports.add(javaType);
        }
    }

    public TreeSet<String> getImports() {
        return imports;
    }
    
}
