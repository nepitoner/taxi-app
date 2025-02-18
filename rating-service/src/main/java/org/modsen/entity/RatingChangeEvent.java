package org.modsen.entity;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.UUID;

@Document(collection = RatingChangeEvent.COLLECTION_NAME)
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PUBLIC)
public class RatingChangeEvent {

    public static final String COLLECTION_NAME = "rating_change_events";

    @Id
    private String eventId;

    @Field("participant_id")
    private UUID participantId;

    @Field("rating")
    private float rating;

    @CreatedDate
    @Field("changed_at")
    private LocalDateTime changedAt;

}
