package cn.org.rapid_framework.generator.util;

import java.util.Random;

/**
 * 
 * @author badqiu
 * @email badqiu(a)gmail.com
 */
public class StringHelper {
	
	public static String emptyIf(String value,String defaultValue) {
		if(value == null || "".equals(value)) {
			return defaultValue;
		}
		return value;
	}
	
	public static String makeAllWordFirstLetterUpperCase(String sqlName) {
		String[] strs = sqlName.toLowerCase().split("_");
		String result = "";
		String preStr = "";
		for(int i = 0; i < strs.length; i++) {
			if(preStr.length() == 1) {
				result += strs[i];
			}else {
				result += capitalize(strs[i]);
			}
			preStr = strs[i];
		}
		return result;
	}
	
	public static String makeAllWordLowerCase(String sqlName) {
		String[] strs = sqlName.toLowerCase().split("_");
		String result = "";
		String preStr = "";
		for(int i = 0; i < strs.length; i++) {
			if(preStr.length() == 1) {
				result += strs[i];
			}else {
				result += strs[i];
			}
			preStr = strs[i];
		}
		return result;
	}
	
	public static String replace(String inString, String oldPattern, String newPattern) {
		if (inString == null) {
			return null;
		}
		if (oldPattern == null || newPattern == null) {
			return inString;
		}

		StringBuffer sbuf = new StringBuffer();
		// output StringBuffer we'll build up
		int pos = 0; // our position in the old string
		int index = inString.indexOf(oldPattern);
		// the index of an occurrence we've found, or -1
		int patLen = oldPattern.length();
		while (index >= 0) {
			sbuf.append(inString.substring(pos, index));
			sbuf.append(newPattern);
			pos = index + patLen;
			index = inString.indexOf(oldPattern, pos);
		}
		sbuf.append(inString.substring(pos));

		// remember to append any characters to the right of a match
		return sbuf.toString();
	}
	/**copy from spring*/
	public static String capitalize(String str) {
		return changeFirstCharacterCase(str, true);
	}
	
	/**copy from spring*/
	public static String uncapitalize(String str) {
		return changeFirstCharacterCase(str, false);
	}
	/**copy from spring*/
	private static String changeFirstCharacterCase(String str, boolean capitalize) {
		if (str == null || str.length() == 0) {
			return str;
		}
		StringBuffer buf = new StringBuffer(str.length());
		if (capitalize) {
			buf.append(Character.toUpperCase(str.charAt(0)));
		}
		else {
			buf.append(Character.toLowerCase(str.charAt(0)));
		}
		buf.append(str.substring(1));
		return buf.toString();
	}
	
	private static final Random RANDOM = new Random();
	public static String randomNumeric(int count) {
        return random(count, false, true);
    }
	
	public static String random(int count, boolean letters, boolean numbers) {
	    return random(count, 0, 0, letters, numbers);
	}
	
    public static String random(int count, int start, int end, boolean letters, boolean numbers) {
        return random(count, start, end, letters, numbers, null, RANDOM);
    }
	 
	public static String random(int count, int start, int end, boolean letters,
			boolean numbers, char[] chars, Random random) {
		if (count == 0) {
			return "";
		} else if (count < 0) {
			throw new IllegalArgumentException(
					"Requested random string length " + count
							+ " is less than 0.");
		}
		if ((start == 0) && (end == 0)) {
			end = 'z' + 1;
			start = ' ';
			if (!letters && !numbers) {
				start = 0;
				end = Integer.MAX_VALUE;
			}
		}

		char[] buffer = new char[count];
		int gap = end - start;

		while (count-- != 0) {
			char ch;
			if (chars == null) {
				ch = (char) (random.nextInt(gap) + start);
			} else {
				ch = chars[random.nextInt(gap) + start];
			}
			if ((letters && Character.isLetter(ch))
					|| (numbers && Character.isDigit(ch))
					|| (!letters && !numbers)) {
				if (ch >= 56320 && ch <= 57343) {
					if (count == 0) {
						count++;
					} else {
						// low surrogate, insert high surrogate after putting it
						// in
						buffer[count] = ch;
						count--;
						buffer[count] = (char) (55296 + random.nextInt(128));
					}
				} else if (ch >= 55296 && ch <= 56191) {
					if (count == 0) {
						count++;
					} else {
						// high surrogate, insert low surrogate before putting
						// it in
						buffer[count] = (char) (56320 + random.nextInt(128));
						count--;
						buffer[count] = ch;
					}
				} else if (ch >= 56192 && ch <= 56319) {
					// private high surrogate, no effing clue, so skip it
					count++;
				} else {
					buffer[count] = ch;
				}
			} else {
				count++;
			}
		}
		return new String(buffer);
	}
	
	/**
	 * Convert a name in camelCase to an underscored name in lower case.
	 * Any upper case letters are converted to lower case with a preceding underscore.
	 * @param filteredName the string containing original name
	 * @return the converted name
	 */
	public static String toUnderscoreName(String name) {
		if(name == null) return null;
		
		String filteredName = name;
		if(filteredName.indexOf("_") >= 0 && filteredName.equals(filteredName.toUpperCase())) {
			filteredName = filteredName.toLowerCase();
		}
		if(filteredName.indexOf("_") == -1 && filteredName.equals(filteredName.toUpperCase())) {
			filteredName = filteredName.toLowerCase();
		}
		
		StringBuffer result = new StringBuffer();
		if (filteredName != null && filteredName.length() > 0) {
			result.append(filteredName.substring(0, 1).toLowerCase());
			for (int i = 1; i < filteredName.length(); i++) {
				String preString = filteredName.substring(i - 1, i);
				String s = filteredName.substring(i, i + 1);
				if(s.equals("_")) {
					result.append("_");
					continue;
				}
				if(preString.equals("_")){
					result.append(s.toLowerCase());
					continue;
				}
				if (s.equals(s.toUpperCase())) {
					result.append("_");
					result.append(s.toLowerCase());
				}
				else {
					result.append(s);
				}
			}
		}
		return result.toString();
	}
	
	/**
	 * @author 杨书江
     * @since  2009年7月14日14:07:55
     * 
     * 验证一个字符串是否为0-9数字组成的
     * @param str  验证的字符串
     * @return    是返回 true  不是返回  false  
     */
    public static boolean isNumber(String str){
    	boolean isNum=false;
    	if(str!=null&&str.trim().matches("^[0-9]+$")){
    		isNum=true;
    	}
    	return isNum;
    }
    /**
     * @author 杨书江
     * @since  2009年7月14日14:07:55
     * 
     * 验证一个字符串是否为a-zA-z英文字母组成的
     * @param str  验证的字符串
     * @return    是返回 true  不是返回  false  
     */
    public static boolean isEnglishChars(String str){
    	boolean isChar=false;
    	if(str!=null&&str.trim().matches("^[a-zA-Z]+$")){
    		isChar=true;
    	}
    	return isChar;
    }
    /**
     * @author 杨书江
     * @since  2009年7月14日14:07:55
     * 
     * 验证一个字符串是否为汉字组成的
     * @param str  验证的字符串
     * @return    是返回 true  不是返回  false  
     */
    public static boolean isChinese(String str){
    	boolean isChinese=false;
    	if(str!=null&&str.trim().matches("^[\u4e00-\u9fa5]+$")){
    		isChinese=true;
    	}
    	return isChinese;
    }
	
}

