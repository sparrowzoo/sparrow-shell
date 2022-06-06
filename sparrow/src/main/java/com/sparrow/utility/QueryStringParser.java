package com.sparrow.utility;

import com.sparrow.cg.MethodAccessor;
import com.sparrow.container.Container;
import com.sparrow.core.Pair;
import com.sparrow.core.TypeConverter;
import com.sparrow.core.spi.ApplicationContext;
import com.sparrow.protocol.POJO;
import com.sparrow.protocol.constant.CONSTANT;
import com.sparrow.protocol.constant.magic.SYMBOL;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * query string parser.
 *
 * @author harry
 */
public class QueryStringParser {

  private static Logger logger = LoggerFactory.getLogger(QueryStringParser.class);


  /**
   * de serial.
   *
   * @param requestToken request token
   * @return map
   */
  public static Map<String, String> deserial(String requestToken) {
    Map<String, String> tokens = new TreeMap<String, String>();

    if (StringUtility.isNullOrEmpty(requestToken)) {
      return null;
    }

    requestToken = requestToken.trim();
    if (requestToken.startsWith(SYMBOL.QUESTION_MARK) || requestToken.startsWith(SYMBOL.AND)) {
      requestToken = requestToken.substring(1);
    }

    String[] requestArray = requestToken.split(SYMBOL.AND);
    for (String pair : requestArray) {
      Pair<String, String> p = Pair.split(pair, SYMBOL.EQUAL);
      tokens.put(p.getFirst(), p.getSecond());
    }
    return tokens;
  }

  public static String serial(Map<String, String> parameters) {
    return serial(parameters, true);
  }


  public static String serial(Map<String, String> parameters,
      boolean isEncode) {
    return serial(parameters, isEncode, null);
  }

  /**
   * serial.
   *
   * @param parameters parameters
   * @param isEncode is encode
   * @param exceptKeyList except key list
   * @return string
   */
  public static String serial(Map<String, String> parameters,
      boolean isEncode, List<String> exceptKeyList) {
    StringBuilder serialParameters = new StringBuilder();
    for (String key : parameters.keySet()) {
      String v = parameters.get(key);
      if (StringUtility.isNullOrEmpty(v)) {
        continue;
      }
      if (exceptKeyList != null) {
        if (exceptKeyList.contains(key)) {
          continue;
        }
      }
      if (serialParameters.length() > 0) {
        serialParameters.append(SYMBOL.AND);
      }
      if (isEncode) {
        try {
          serialParameters.append(key + SYMBOL.EQUAL
              + URLEncoder.encode(v, CONSTANT.CHARSET_UTF_8));
        } catch (UnsupportedEncodingException ignore) {
          logger.error("serial error", ignore);
        }

      } else {
        serialParameters.append(key + SYMBOL.EQUAL + v);
      }
    }
    return serialParameters.toString();
  }


  /**
   * replace query string.
   *
   * @param queryString query string
   * @param currentQuery current query
   * @return new query
   */
  public static String replace(String queryString, String currentQuery) {
    if (queryString == null) {
      queryString = SYMBOL.EMPTY;
    }
    if (StringUtility.isNullOrEmpty(currentQuery)) {
      if (!StringUtility.isNullOrEmpty(queryString)) {
        queryString = "?" + queryString;
      }
      return queryString;
    }
    Map<String, String> map = new HashMap<String, String>();

    if (!StringUtility.isNullOrEmpty(queryString)) {
      map = deserial(queryString);
    }

    Pair<String, String> pair = Pair.split(currentQuery, "=");
    if (map == null) {
      return SYMBOL.EMPTY;
    }
    if (map.containsKey(pair.getFirst())) {
      map.remove(pair.getFirst());
    }
    if (!StringUtility.isNullOrEmpty(pair.getSecond())) {
      map.put(pair.getFirst(), pair.getSecond());
    }
    return serial(map);
  }

  /**
   * get parameter.
   *
   * @param entity pojo
   */
  public static String getParameter(POJO entity) {
    Container container = ApplicationContext.getContainer();
    List<TypeConverter> fieldList = container.getFieldList(entity.getClass());
    MethodAccessor methodAccessor = container.getProxyBean(entity.getClass());
    StringBuilder sb = new StringBuilder();
    for (TypeConverter field : fieldList) {
      Object o = methodAccessor.get(entity, field.getName());
      if (StringUtility.isNullOrEmpty(o)) {
        continue;
      }
      if (sb.length() > 0) {
        sb.append(SYMBOL.DOLLAR);
      }
      try {
        sb.append(StringUtility.setFirstByteLowerCase(field.getName())
            + SYMBOL.EQUAL
            + URLEncoder.encode(String.valueOf(o), CONSTANT.CHARSET_UTF_8));
      } catch (UnsupportedEncodingException ignore) {
        logger.error("get parameter error", ignore);
      }
    }
    return sb.toString();
  }
}
