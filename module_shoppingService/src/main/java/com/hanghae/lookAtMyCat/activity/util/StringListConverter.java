package com.hanghae.lookAtMyCat.activity.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.List;

@Converter
public class StringListConverter implements AttributeConverter<List<String>, String> {

//    private final ObjectMapper mapper = new ObjectMapper();
    private static final String SPLIT_CHAR = ", ";

    @Override
    public String convertToDatabaseColumn(List<String> dataList) {
//        try {
//            return mapper.writeValueAsString(dataList);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
        return String.join(SPLIT_CHAR, dataList);
    }

    @Override
    public List<String> convertToEntityAttribute(String data) {
//        try {
//            return mapper.readValue(data, List.class);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
        return Arrays.asList(data.split(SPLIT_CHAR));
    }
}
