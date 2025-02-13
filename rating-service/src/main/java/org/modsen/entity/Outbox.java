package org.modsen.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.UUID;

@Document(collection = Outbox.COLLECTION_NAME)
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PUBLIC)
public class Outbox {

    public static final String COLLECTION_NAME = "outboxes";

    @Id
    private int outboxId;

    @Field("participant_id")
    private UUID participantId;

    @Field("rating")
    private float rating;

}
