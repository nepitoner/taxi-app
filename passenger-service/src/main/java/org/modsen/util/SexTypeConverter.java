package org.modsen.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.modsen.entity.SexType;

import java.util.Objects;

@Converter
public class SexTypeConverter implements AttributeConverter<SexType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(SexType attribute) {
        return Objects.requireNonNullElse(attribute, SexType.OTHER).getCode();
    }

    @Override
    public SexType convertToEntityAttribute(Integer dbData) {
        return SexType.fromCode(dbData);
    }

}
