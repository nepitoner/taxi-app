package org.example.utils;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class SexTypeConverter implements AttributeConverter<SexType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(SexType attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getCode();
    }

    @Override
    public SexType convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        return SexType.fromCode(dbData);
    }
}
