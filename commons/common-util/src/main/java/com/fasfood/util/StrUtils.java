package com.fasfood.util;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StrUtils {
    public static final String DOT = ".";
    public static final String SLASH = "/";
    public static final String ROOT = "root";
    public static final String EMPTY = "";
    public static final String STAR = "*";
    public static final char CHAR_SHACKLE = '@';
    public static final char COMMA = ',';
    public static final Integer CHAR_REPLACE_START = 3;
    private static final Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
    public static final String CURRENCY_FORMAT = "###,###,###";
    private static final Pattern TAGS_PATTERN = Pattern.compile("<.+?>");

    private StrUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static boolean isBlank(Object str) {
        return str == null || ((String) str).trim().isEmpty();
    }

    public static String getGeneralField(String getOrSetMethodName) {
        return !getOrSetMethodName.startsWith("get") && !getOrSetMethodName.startsWith("set") ? null : cutPreAndLowerFirst(getOrSetMethodName, 3);
    }

    public static String cutPreAndLowerFirst(String str, int preLength) {
        if (str == null) {
            return null;
        } else if (str.length() > preLength) {
            char first = Character.toLowerCase(str.charAt(preLength));
            return str.length() > preLength + 1 ? first + str.substring(preLength + 1) : String.valueOf(first);
        } else {
            return null;
        }
    }

    public static String genSetter(String fieldName) {
        return upperFirstAndAddPre(fieldName, "set");
    }

    public static String upperFirstAndAddPre(String str, String preString) {
        return str != null && preString != null ? preString + upperFirst(str) : null;
    }

    public static String upperFirst(String str) {
        char var10000 = Character.toUpperCase(str.charAt(0));
        return var10000 + str.substring(1);
    }

    public static String genGetter(String fieldName) {
        return upperFirstAndAddPre(fieldName, "get");
    }

    public static String lowerFirst(String str) {
        char var10000 = Character.toLowerCase(str.charAt(0));
        return var10000 + str.substring(1);
    }

    public static String removePrefix(String str, String prefix) {
        return str != null && str.startsWith(prefix) ? str.substring(prefix.length()) : str;
    }

    public static String removePrefixIgnoreCase(String str, String prefix) {
        return str != null && str.toLowerCase().startsWith(prefix.toLowerCase()) ? str.substring(prefix.length()) : str;
    }

    public static String removeSuffix(String str, String suffix) {
        return str != null && str.endsWith(suffix) ? str.substring(0, str.length() - suffix.length()) : str;
    }

    public static String removeSuffixIgnoreCase(String str, String suffix) {
        return str != null && str.toLowerCase().endsWith(suffix.toLowerCase()) ? str.substring(0, str.length() - suffix.length()) : str;
    }

    public static List<String> split(String str, char separator) {
        return split(str, separator, 0);
    }

    public static List<String> split(String str, char separator, int limit) {
        if (str == null) {
            return new ArrayList<>();
        } else {
            List<String> list = new ArrayList<>(limit == 0 ? 16 : limit);
            if (limit == 1) {
                list.add(str);
                return list;
            } else {
                boolean isNotEnd = true;
                int strLen = str.length();
                StringBuilder sb = new StringBuilder(strLen);

                for(int i = 0; i < strLen; ++i) {
                    char c = str.charAt(i);
                    if (isNotEnd && c == separator) {
                        list.add(sb.toString());
                        sb.delete(0, sb.length());
                        if (limit != 0 && list.size() == limit - 1) {
                            isNotEnd = false;
                        }
                    } else {
                        sb.append(c);
                    }
                }

                list.add(sb.toString());
                return list;
            }
        }
    }

    public static String[] split(String str, String delimiter) {
        if (str == null) {
            return new String[0];
        } else if (str.trim().isEmpty()) {
            return new String[]{str};
        } else {
            int delLength = delimiter.length();
            int maxParts = str.length() / delLength + 2;
            int[] positions = new int[maxParts];
            int j = 0;
            int count = 0;

            int i;
            for(positions[0] = -delLength; (i = str.indexOf(delimiter, j)) != -1; j = i + delLength) {
                ++count;
                positions[count] = i;
            }

            ++count;
            positions[count] = str.length();
            String[] result = new String[count];

            for(int var9 = 0; var9 < count; ++var9) {
                result[var9] = str.substring(positions[var9] + delLength, positions[var9 + 1]);
            }

            return result;
        }
    }

    public static String repeat(char c, int count) {
        char[] result = new char[count];

        Arrays.fill(result, c);

        return new String(result);
    }

    public static String convertCharset(String str, String sourceCharset, String destCharset) {
        if (!isBlank(str) && !isBlank(sourceCharset) && !isBlank(destCharset)) {
            try {
                return new String(str.getBytes(sourceCharset), destCharset);
            } catch (UnsupportedEncodingException var4) {
                return str;
            }
        } else {
            return str;
        }
    }

    public static boolean equalsNotEmpty(String str1, String str2) {
        return !isEmpty(str1) && str1.equals(str2);
    }

    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static String format(String template, Object... values) {
        return String.format(template.replace("{}", "%s"), values);
    }

    public static boolean compareText(String key, String line, double scoreRate) {
        key = key.trim().toLowerCase();
        line = line.trim().toLowerCase();
        int levenshteinDistance = levenshteinDistance(key, line);
        int length = Math.max(key.length(), line.length());
        double score = (double)1.0F - (double)levenshteinDistance / (double)length;
        return score > scoreRate;
    }

    public static int levenshteinDistance(String x, String y) {
        int[][] dp = new int[x.length() + 1][y.length() + 1];

        for(int i = 0; i <= x.length(); ++i) {
            for(int j = 0; j <= y.length(); ++j) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    dp[i][j] = min(dp[i - 1][j - 1] + costOfSubstitution(x.charAt(i - 1), y.charAt(j - 1)), dp[i - 1][j] + 1, dp[i][j - 1] + 1);
                }
            }
        }

        return dp[x.length()][y.length()];
    }

    public static int min(int... numbers) {
        return Arrays.stream(numbers).min().orElse(Integer.MAX_VALUE);
    }

    public static int costOfSubstitution(char a, char b) {
        return a == b ? 0 : 1;
    }

    public static String getCollectionTypeCode(String refNo) {
        if (isBlank(refNo)) {
            return null;
        } else {
            return refNo.length() < 7 ? null : refNo.substring(3, 7);
        }
    }

    public static boolean containText(String left, String right) {
        if (left != null && right != null) {
            left = left.toLowerCase();
            right = right.toLowerCase();
            return left.contains(right) || right.contains(left);
        } else {
            return true;
        }
    }

    public static String foldingAscii(String value) {
        if (isBlank(value)) {
            return value;
        } else {
            String nfdNormalizedString = Normalizer.normalize(value.toLowerCase(), Form.NFD);
            Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
            return pattern.matcher(nfdNormalizedString).replaceAll("").replace('đ', 'd').replaceAll(" +", " ");
        }
    }

    public static String removeAccent(String s) {
        String temp = Normalizer.normalize(s, Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("");
    }

    public static String emailFormat(String email) {
        StringBuilder emailFormat = new StringBuilder();
        if (!isEmpty(email)) {
            for(int i = 0; i < email.length(); ++i) {
                if (i >= CHAR_REPLACE_START && email.charAt(i) != '@') {
                    emailFormat.append("*");
                } else {
                    if (email.charAt(i) == '@') {
                        emailFormat.append(email.substring(i));
                        break;
                    }

                    emailFormat.append(email.charAt(i));
                }
            }
        }

        return emailFormat.toString();
    }

    public static String phoneNumberFormat(String phoneNumber) {
        StringBuilder phoneFormat = new StringBuilder();
        if (!isEmpty(phoneNumber)) {
            for(int i = 0; i < phoneNumber.length(); ++i) {
                if (i >= CHAR_REPLACE_START) {
                    phoneFormat.append("*");
                } else {
                    phoneFormat.append(phoneNumber.charAt(i));
                }
            }
        }

        return phoneFormat.toString();
    }

    public static String addressFormat(String address) {
        if (!isEmpty(address)) {
            for(char c : address.toCharArray()) {
                if (c == ',') {
                    String[] strings = address.split(String.valueOf(','));
                    if (strings.length >= 2) {
                        return strings[strings.length - 2] + "," + strings[strings.length - 1];
                    }
                }
            }
        }

        return address;
    }

    public static boolean isNumeric(String strNum) {
        return strNum != null && pattern.matcher(strNum).matches();
    }

    public static String generateCodeFromId(long id, int padLeft) {
        char[] c = "123456789qwertyuiopasdfghjklzxcvbnm".toUpperCase().toCharArray();
        StringBuilder code = new StringBuilder();
        int length = c.length;
        long index = id;

        while(true) {
            int lastIndex = (int)(index % (long)length) - 1;
            if (lastIndex < 0) {
                lastIndex = c.length - 1;
            }

            code.insert(0, c[lastIndex]);
            if (index <= (long)length) {
                break;
            }

            index /= (long)length;
            if (index < (long)length) {
                code.insert(0, c[(int) index - 1]);
                break;
            }
        }

        return StringUtils.leftPad(code.toString(), padLeft, '0');
    }

    public static String generateNewCodeFromFixedSize(String code, Instant createdAt, int maxSize) {
        long durationMs;
        if (Objects.nonNull(createdAt)) {
            durationMs = createdAt.toEpochMilli();
        } else {
            durationMs = Instant.now().toEpochMilli();
        }

        String radix36 = Long.toString(durationMs, 36);
        return generateCodeFromFixedSize(code, String.format("_%s", radix36), maxSize);
    }

    public static String generateCodeFromFixedSize(String code, String randomString, int maxSize) {
        StringBuilder generateCode = new StringBuilder();
        if (isBlank(randomString)) {
            randomString = IdUtils.nextId().toString();
        }

        if (isBlank(code)) {
            return randomString.length() <= maxSize ? randomString : randomString.substring(0, maxSize);
        } else if (code.length() > maxSize) {
            if (randomString.length() <= maxSize) {
                generateCode.append(code, 0, maxSize - randomString.length()).append(randomString);
                return generateCode.toString();
            } else {
                return code.substring(0, maxSize);
            }
        } else if (randomString.length() > maxSize - code.length()) {
            generateCode.append(code).append(randomString, 0, maxSize - code.length());
            return generateCode.toString();
        } else {
            return generateCode.append(code).append(randomString).toString();
        }
    }

    public static String parseDataToString(Object data) {
        if (Objects.nonNull(data)) {
            return data instanceof LocalDate ? DateUtils.formatLocalDate((LocalDate)data) : data.toString();
        } else {
            return "";
        }
    }

    public static String formatMoney(BigDecimal price) {
        if (Objects.nonNull(price)) {
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            return decimalFormat.format(price).replace(",", ".");
        } else {
            return "0";
        }
    }

    public static String concatenateString(Collection<String> stringList, String delimiter) {
        StringJoiner stringJoiner = new StringJoiner(delimiter);

        for(String str : stringList) {
            if (str != null && !str.isBlank()) {
                stringJoiner.add(str);
            }
        }

        return stringJoiner.toString();
    }

    public static String capitalizeFully(String input) {
        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = true;

        for(char c : input.toCharArray()) {
            if (Character.isWhitespace(c)) {
                capitalizeNext = true;
                result.append(c);
            } else if (capitalizeNext) {
                result.append(Character.toUpperCase(c));
                capitalizeNext = false;
            } else {
                result.append(Character.toLowerCase(c));
            }
        }

        return result.toString();
    }

    public static String capitalizeFirstLetter(String str) {
        if (str != null && !str.isEmpty()) {
            char var10000 = Character.toUpperCase(str.charAt(0));
            return var10000 + str.substring(1).toLowerCase();
        } else {
            return str;
        }
    }

    public static String removeTags(String string) {
        if (string != null && !string.isEmpty()) {
            Matcher m = TAGS_PATTERN.matcher(string);
            return m.replaceAll("");
        } else {
            return string;
        }
    }

    public static String joinKey(String prefix, String... suffixes) {
        String[] parts = new String[suffixes.length + 1];
        parts[0] = prefix;
        System.arraycopy(suffixes, 0, parts, 1, suffixes.length);
        return String.join(":", parts);
    }
}
