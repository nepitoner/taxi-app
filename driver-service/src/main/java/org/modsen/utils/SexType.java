package org.modsen.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static org.modsen.utils.constant.ExceptionConstant.UNKNOWN_CODE_MESSAGE;

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
        throw new IllegalArgumentException(UNKNOWN_CODE_MESSAGE.formatted(code));
    }

}
