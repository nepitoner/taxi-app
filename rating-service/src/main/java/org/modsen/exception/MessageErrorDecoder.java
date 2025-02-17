package org.modsen.exception;

import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder errorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        FeignException exception = feign.FeignException.errorStatus(methodKey, response);
        String message = parse(exception.getMessage());

        return switch (response.status()) {
            case 400 -> new IllegalArgumentException(!message.isBlank() ? message : "Bad request");
            case 404 -> new RideNotFoundException(!message.isBlank() ? message : "Not found");
            default -> errorDecoder.decode(methodKey, response);
        };
    }

    public static String parse(String errorMessage) {
        String regex = "\"message\":\"(.*?)\"";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(errorMessage);

        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }

}
