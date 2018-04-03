package com.bitcoin.common;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EnvironmentVariableParser {

  /**
   * Parses a string, resolves and expands any environment variables.
   *
   * @param input String to resolve
   * @return String with environment variables expanded to their actual values.
   */
  public static String resolveEnvVars(String input) {
    return resolveEnvVars(input, System.getenv());
  }

  /**
   * Parses a string and resolves any environment variables.
   * Allows for a custom environment map.
   */
  public static String resolveEnvVars(String input, Map envVars) {
    if (input == null) {
      return null;
    }

    Pattern pattern = Pattern.compile("\\$\\{(\\w+)\\}|\\$(\\w+)");
    Matcher matcher = pattern.matcher(input);
    StringBuffer buffer = new StringBuffer();
    while (matcher.find()) {
      String envVarName = null == matcher.group(1) ? matcher.group(2) : matcher.group(1);
      String envVarValue = (String) envVars.get(envVarName);
      matcher.appendReplacement(buffer, null == envVarValue ? "" : envVarValue);
    }
    matcher.appendTail(buffer);
    return buffer.toString();
  }
}
