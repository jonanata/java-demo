package com.ioc.iotserver.utils;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {

	private static Logger logger = LoggerFactory.getLogger(JsonUtils.class);

	public static JsonNode toObj(String jsonString) throws IOException {

		return (new ObjectMapper()).readTree(jsonString);

	}

    public static String toString(Object result) throws JsonProcessingException {

		return (new ObjectMapper()).writeValueAsString(result);

	}

    public static <T> T toObj(JsonNode jsonNode, Class<T> c) throws IOException {

		return (new ObjectMapper()).treeToValue(jsonNode, c);

	}

    public static <T> T stringToObj(String jsonNode, Class<T> c) throws IOException {

		return (new ObjectMapper()).readValue(jsonNode, c);

	}

    public static <T> T toObj(JsonNode jsonNode, TypeReference<?> toValueTypeRef) throws IOException {

    	return (T) (new ObjectMapper()).convertValue(jsonNode, toValueTypeRef);

	}

    public static <T> JsonNode isValidJSON(String json) throws Throwable {

    	ObjectMapper mapper = new ObjectMapper();

		mapper.enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS);
        mapper.enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY);

        return mapper.readTree(json);


    }

    public static <T> T isValidJSON(String json, Class<T> clazz) throws Throwable {

    	ObjectMapper mapper = new ObjectMapper();

		mapper.enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS);
        mapper.enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY);

        return mapper.readValue(json, clazz);


    }

}
