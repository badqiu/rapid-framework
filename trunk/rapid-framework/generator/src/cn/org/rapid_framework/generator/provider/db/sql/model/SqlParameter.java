package cn.org.rapid_framework.generator.provider.db.sql.model;

import java.sql.ParameterMetaData;
import java.sql.SQLException;

import cn.org.rapid_framework.generator.provider.db.table.model.Column;

public class SqlParameter extends Column {
//    	String parameterClassName;
//    	int parameterMode;
//    	int parameterType;
//    	String parameterTypeName;
//    	int precision;
//    	int scale;
    	String paramName;
    	boolean isListParam = false;
    	boolean cannotGuessParamType = false; //是否是不能从数据库metadata猜测出数据类型的column
    	
    	public SqlParameter() {}

        public SqlParameter(Column param) {
            super(param);
        }
        
    	public SqlParameter(SqlParameter param) {
    	    super(param);
    		this.isListParam = param.isListParam;
    		this.paramName = param.paramName;
    		
//    		this.parameterClassName = param.parameterClassName;
//    		this.parameterMode = param.parameterMode;
//    		this.parameterType = param.parameterType;
//    		this.parameterTypeName = param.parameterTypeName;
//    		this.precision = param.precision;
//    		this.scale = param.scale;
    	}
    	
//    	public SqlParameter(ParameterMetaData m,int i) throws SQLException {
//    		this.parameterClassName = m.getParameterClassName(i);
//    		this.parameterMode = m.getParameterMode(i);
//    		this.parameterType = m.getParameterType(i);
//    		this.parameterTypeName = m.getParameterTypeName(i);
//    		this.precision = m.getPrecision(i);
//    		this.scale = m.getScale(i);
//    	}
//		public String getParameterClassName() {
//			return parameterClassName;
//		}
//		public void setParameterClassName(String parameterClassName) {
//			this.parameterClassName = parameterClassName;
//		}
		public String getPreferredParameterClassName() {
		    String parameterClassName = getJavaType();
			if(isListParam) {
				if(parameterClassName.indexOf("[]") >= 0){
					return parameterClassName;
				}
				if(parameterClassName.indexOf("List") >= 0){
					return parameterClassName;
				}
				if(parameterClassName.indexOf("Set") >= 0){
					return parameterClassName;
				}
				return "java.util.List<"+parameterClassName+">";
			}else {
				return parameterClassName;
			}
		}
//		public int getParameterMode() {
//			return parameterMode;
//		}
//		public void setParameterMode(int parameterMode) {
//			this.parameterMode = parameterMode;
//		}
//		public int getParameterType() {
//			return parameterType;
//		}
//		public void setParameterType(int parameterType) {
//			this.parameterType = parameterType;
//		}
//		public String getParameterTypeName() {
//			return parameterTypeName;
//		}
//		public void setParameterTypeName(String parameterTypeName) {
//			this.parameterTypeName = parameterTypeName;
//		}
//		public int getPrecision() {
//			return precision;
//		}
//		public void setPrecision(int precision) {
//			this.precision = precision;
//		}
//		public int getScale() {
//			return scale;
//		}
//		public void setScale(int scale) {
//			this.scale = scale;
//		}
		public String getParamName() {
			return paramName;
		}
		public void setParamName(String paramName) {
			this.paramName = paramName;
		}
		public boolean isListParam() {
			return isListParam;
		}
		public void setListParam(boolean isListParam) {
			this.isListParam = isListParam;
		}
		
		public boolean equals(Object obj) {
			if(obj == this) return true;
			if(obj == null) return false;
			if(obj instanceof SqlParameter) {
				SqlParameter other = (SqlParameter)obj;
				return paramName.equals(other.getParamName());
			}else {
				return false;
			}
		}
		public int hashCode() {
			return paramName.hashCode();
		}
		public String toString() {
			return "paramName:"+paramName+" parameterClassName:"+getPreferredParameterClassName();
		}
    }