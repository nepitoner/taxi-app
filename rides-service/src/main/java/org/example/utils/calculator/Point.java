package org.example.utils.calculator;

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