package cn.org.rapid_framework.dynamic.sql;
public class ColumnCondition {
        private String column;
        private Operator operator;
        private Object value; //TODO
        
        public String toString() {
            return getColumnExpression();
        }
        
        public String getColumnExpression() {
            if(operator == null) return null;
            return operator.getColumnExpression(column);
        }
        
    }