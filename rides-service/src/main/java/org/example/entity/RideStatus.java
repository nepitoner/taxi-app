package org.example.entity;

import static org.example.utils.constant.ExceptionConstant.UNKNOWN_CODE_MESSAGE;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.exception.IncorrectStatusException;

@Getter
@RequiredArgsConstructor
public enum RideStatus {

    CREATED(0),
    ACCEPTED(100),
    ON_WAY_TO_PASSENGER(200),
    ON_WAY_TO_DESTINATION(300),
    COMPLETED(400),
    CANCELED(500);

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
