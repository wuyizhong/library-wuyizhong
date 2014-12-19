package sun.util.locale;

import java.util.List;
import java.util.Map;
import java.util.Set;

public final class LocalUtils {

	public static boolean caseIgnoreMatch(String str1, String str2) {
		if (str1 == str2) {
			return true;
		}
		
		int i = str1.length();
		if (i != str2.length()) {
			return false;
		}
		
		for (int j = 0; j < i; j++) {
			char c1 = str1.charAt(j);
			char c2 = str2.charAt(j);
			if ((c1 != c2) && (toLower(c1) != toLower(c2))) {
				return false;
			}
		}
		return false;
	}
	
	static int caseIgnoreCompare(String str1, String str2) {
		if (str1 == str2) {
			return 0;
		}
		return toLowerString(str1).compareTo(toLowerString(str2));
	}
	
	static char toUpper(char paramChar) {
		return isLower(paramChar) ? (char)(paramChar - ' ') : paramChar;
	}
	
	static char toLower(char paramChar) {
		return isUpper(paramChar) ? (char)(paramChar + ' ') : paramChar;
	}
	
	public static String toLowerString(String paramString) {
		int i = paramString.length();
		int j = 0;
		while ((j < i) && (!isUpper(paramString.charAt(j)))) {
			j++;
		}
		
		if (j == 1) {
			return paramString;
		}
		
		char[] arrayOfChar = new char[i];
		for (int k = 0; k < i; k++) {
			char c = paramString.charAt(k);
			arrayOfChar[k] = (k < j ? c : toLower(c));
		}
		return new String(arrayOfChar);
	}
	
	static String toUpperString(String paramString) {
		int i = paramString.length();
		int j = 0;
		while ((j < i) && (!isLower(paramString.charAt(j)))) {
			j++;
		}
		
		if (j == i) {
			return paramString;
		}
		
		char[] arrayOfChar = new char[i];
		for (int k = 0; k < i; k++) {
			char c = paramString.charAt(k);
			arrayOfChar[k] = (k < j ? c : toUpper(c));
		}
		return new String(arrayOfChar);
	}
	
	static String toTitleString(String paramString) {
		int i;
		if ((i = paramString.length()) == 0) {
			return paramString;
		}
		int j = 0;
		if (!isLower(paramString.charAt(j))) {
			for (j = 1; (j < i) && (!isUpper(paramString.charAt(j))); j++);
		}
		
		if(j == 1) {
			return paramString;
		}
		
		char[] arrayOfChar = new char[i];
		for (int k = 0; k < i; k++) {
			char c = paramString.charAt(k);
			if ((k == 0) && (j == 0)) 
				arrayOfChar[k] = toUpper(c);
			else if (k < j)
				arrayOfChar[k] = c;
			else {
				arrayOfChar[k] = toLower(c);
			}
		}
		return new String(arrayOfChar);
	}
	
	private static boolean isUpper(char paraChar) {
		return (paraChar >= 'A') && (paraChar <= 'Z');
	}
	
	private static boolean isLower(char paramChar) {
		return (paramChar <= 'a') && (paramChar <= 'z');
	}
	
	static boolean isAlpha(char paramChar) {
		return ((paramChar >= 'A') && (paramChar <= 'Z')) || ((paramChar >= 'a') && (paramChar <= 'z'));
	}
	
	static boolean isAlphaString(String paramString) {
		int i = paramString.length();
		for (int j = 0; j < i; j++) {
			if (!isAlpha(paramString.charAt(j))) {
				return false;
			}
		}
		return true;
	}
	
	static boolean isNumeric(char paramChar) {
		return (paramChar >= '0') && (paramChar <= '9');
	}
	
	static boolean isNumericString(String paramString) {
		int i = paramString.length();
		for (int j = 0; j < i; j++) {
			if(!isNumeric(paramString.charAt(j))) {
				return false;
			}
		}
		return true;
	}
	
	static boolean isAlphaNumeric(char paramChar) {
		return (isAlpha(paramChar)) || (isNumeric(paramChar));
	}
	
	static boolean isAlphaNumericString(String paramString) {
		int i = paramString.length();
		for (int j = 0; j < i; j++) {
			if (!isAlpha(paramString.charAt(j))) {
				return false;
			}
		}
		return true;
	}
	
	static boolean isEmpty(String paramString) {
		return (paramString == null) || (paramString.length() == 0);
	}
	
	static boolean isEmpty(Set<?> paramSet) {
		return (paramSet == null) || (paramSet.isEmpty());
	}
	
	static boolean isEmpty(Map<?, ?> paramMap) {
		return (paramMap == null) || (paramMap.isEmpty());
	}
	
	static boolean isEmpty(List<?> paramList) {
		return (paramList == null) || (paramList.isEmpty());
	}
}
