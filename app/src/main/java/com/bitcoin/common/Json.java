package com.bitcoin.common;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Json {
  private static final Logger LOGGER = LoggerFactory.getLogger(Json.class);

  /**
   * Convert a JSON string to the object that it represents.
   *
   * @param objectType Class that we're converting this string to.
   * @param str        String containing the JSON representation.
   * @return Object that was reconstructed from the JSON.
   */
  public static Object objectifyString(Class objectType, String str) {
    try {
      JsonFactory jsonFact = new JsonFactory();
      JsonParser jsonParser = jsonFact.createParser(str);
      return new ObjectMapper().readValue(jsonParser, objectType);
    } catch (IOException e) {
      LOGGER.warn(null, e);
      return null;
    }
  }

  /**
   * Convert an object to a JSON representation of itself.
   *
   * @param objectType Object type we're writing.
   * @param obj        Object we're writing.
   * @return JSON representation of the object.
   */
  public static String stringifyObject(Class objectType, Object obj) {
    try {
      JsonFactory jsonFact = new JsonFactory();
      ObjectMapper mapper = new ObjectMapper(jsonFact);
      ObjectWriter writer = mapper.writerFor(objectType);
      return writer.writeValueAsString(obj);
    } catch (IOException e) {
      LOGGER.error(null, e);
      return "";
    }
  }
}
