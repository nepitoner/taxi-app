package org.modsen.entity;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = Rating.COLLECTION_NAME)
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PUBLIC)
public class Rating {

    public static final String COLLECTION_NAME = "ratings";

    @Version
    private Long version;

    @Id
    @Builder.Default
    private UUID ratingId = UUID.randomUUID();

    @Field("from_id")
    private UUID fromId;

    @Field("to_id")
    private UUID toId;

    @Field("ride_id")
    private UUID rideId;

    @Field("rating")
    private Float rating;

    @Field("comment")
    private String comment;

    @CreatedDate
    @Field("created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Field("updated_at")
    private LocalDateTime updatedAt;

}
