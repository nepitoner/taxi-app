package org.example.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SexType {
    FEMALE(0),
    MALE(1),
    OTHER(2);

    private final int code;

    public static SexType fromCode(int code) {
        for (SexType type : SexType.values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown code " + code);
    }

}
