package org.example.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Document(collection = Ride.COLLECTION_NAME)
@Getter
@Builder
@AllArgsConstructor
@Setter(AccessLevel.PUBLIC)
public class Ride {

    public static final String COLLECTION_NAME = "rides";

    @Version
    private Long version;

    @Id
    private UUID rideId;

    @Field("driver_id")
    private UUID driverId;

    @Field("passenger_id")
    private UUID passengerId;

    @Field("starting_coordinates")
    private List<Double> startingCoordinates;

    @Field("ending_coordinates")
    private List<Double> endingCoordinates;

    @Field("ride_status")
    private RideStatus rideStatus;

    @Field("order_date_time")
    private LocalDateTime orderDateTime;

    @Field("price")
    private BigDecimal price;

    @CreatedDate
    @Field("created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Field("updated_at")
    private LocalDateTime updatedAt;

    public Ride() {
        this.rideId = UUID.randomUUID();
    }
    
}
