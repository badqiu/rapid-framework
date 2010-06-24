package cn.org.rapid_framework.generator.provider.sql.model;

import java.sql.ParameterMetaData;
import java.sql.SQLException;

public class SelectParameter {
    	String parameterClassName;
    	int parameterMode;
    	int parameterType;
    	String parameterTypeName;
    	int precision;
    	int scale;
    	String paramName;
    	public SelectParameter() {}
    	public SelectParameter(ParameterMetaData m,int i) throws SQLException {
    		this.parameterClassName = m.getParameterClassName(i);
    		this.parameterMode = m.getParameterMode(i);
    		this.parameterType = m.getParameterType(i);
    		this.parameterTypeName = m.getParameterTypeName(i);
    		this.precision = m.getPrecision(i);
    		this.scale = m.getScale(i);
    	}
		public String getParameterClassName() {
			return parameterClassName;
		}
		public void setParameterClassName(String parameterClassName) {
			this.parameterClassName = parameterClassName;
		}
		public int getParameterMode() {
			return parameterMode;
		}
		public void setParameterMode(int parameterMode) {
			this.parameterMode = parameterMode;
		}
		public int getParameterType() {
			return parameterType;
		}
		public void setParameterType(int parameterType) {
			this.parameterType = parameterType;
		}
		public String getParameterTypeName() {
			return parameterTypeName;
		}
		public void setParameterTypeName(String parameterTypeName) {
			this.parameterTypeName = parameterTypeName;
		}
		public int getPrecision() {
			return precision;
		}
		public void setPrecision(int precision) {
			this.precision = precision;
		}
		public int getScale() {
			return scale;
		}
		public void setScale(int scale) {
			this.scale = scale;
		}
		public String getParamName() {
			return paramName;
		}
		public void setParamName(String paramName) {
			this.paramName = paramName;
		}
		public String toString() {
			return "paramName:"+paramName+" parameterClassName:"+parameterClassName;
		}
    }