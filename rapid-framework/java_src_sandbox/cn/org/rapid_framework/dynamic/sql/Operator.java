package cn.org.rapid_framework.dynamic.sql;
public enum Operator {
    EQ("="),NOT_EQ("<>"),GT(">"),LT("<"),GTE(">="),LTE("<="),IN("in"),NOT_IN("not in"),LIKE("like"),IS_NULL("is null"),IS_NOT_NULL("is not null");
    
    private String operator;
    private String desc;
    
    Operator(String operator) {
        this.operator = operator;
        this.desc = operator;
    }
    
    public String getOperator() {
        return this.operator;
    }
    
    public String getDesc() {
        return this.desc;
    }
    
    public String getColumnExpression(String column) {
        String placeholder = ":"+column;
        if(this == IN || this == NOT_IN) {
            return column + " " + operator +" ("+placeholder+")";
        }
        if(this == LIKE) {
            return column + " " + operator +" %"+placeholder+"%";
        }
        if(this == IS_NULL || this == IS_NOT_NULL) {
            return column + " " + operator;
        }
        return column + " " + operator +" "+placeholder;
    }
    
}

