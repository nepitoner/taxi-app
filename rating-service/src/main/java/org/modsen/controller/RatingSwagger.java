package org.modsen.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.modsen.dto.request.RatingRequest;
import org.modsen.dto.request.RideCommentRequest;
import org.modsen.dto.response.PagedRatingResponse;
import org.modsen.dto.response.RateResponse;
import org.modsen.dto.response.RatingResponse;
import org.modsen.dto.response.SuccessResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Tag(name = "Rating", description = "Methods for managing ratings")
public interface RatingSwagger {

    @Operation(summary = "Getting paged ratings")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All the ratings were successfully sent"),
            @ApiResponse(responseCode = "400", description = "Validation failed")
    })
    PagedRatingResponse getAllRatings(
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "page.incorrect") int page,
            @RequestParam(defaultValue = "10") @Min(value = 1, message = "limit.incorrect") int limit
    );

    @Operation(summary = "Getting driver's or passenger's rate by his id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The rating was successfully sent"),
            @ApiResponse(responseCode = "400", description = "Validation failed")
    })
    RateResponse getRateById(@PathVariable UUID participantId);

    @Operation(summary = "Create a new rating")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "The rating was successfully created"),
            @ApiResponse(responseCode = "400", description = "Validation failed")
    })
    SuccessResponse createRating(@Valid @RequestBody RatingRequest request, @PathVariable UUID participantId);

    @Operation(summary = "Add comment about the ride")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The comment was successfully added"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Rating with specified id wasn't found")
    })
    RatingResponse addRideComment(@PathVariable UUID ratingId,
                                  @PathVariable UUID fromId,
                                  @Valid @RequestBody RideCommentRequest request
    );

}
