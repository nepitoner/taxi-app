package org.modsen.util.calculator;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Point {

    private double longitude;

    private double latitude;

}