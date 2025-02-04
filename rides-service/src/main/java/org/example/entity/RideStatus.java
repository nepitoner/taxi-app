package org.example.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.exception.IncorrectStatusException;

import static org.example.utils.constant.ExceptionConstant.UNKNOWN_CODE_MESSAGE;

@Getter
@RequiredArgsConstructor
public enum RideStatus {
    CREATED(0),
    ACCEPTED(1),
    ON_WAY_TO_PASSENGER(2),
    ON_WAY_TO_DESTINATION(3),
    COMPLETED(4),
    CANCELED(5);

    private final int code;

    public static RideStatus fromCode(int code) {
        for (RideStatus status : RideStatus.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        throw new IncorrectStatusException(UNKNOWN_CODE_MESSAGE.formatted(code));
    }
}
