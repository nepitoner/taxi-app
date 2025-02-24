package org.modsen.controller.impl;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.modsen.controller.RatingApi;
import org.modsen.dto.request.RatingRequest;
import org.modsen.dto.request.RequestParams;
import org.modsen.dto.request.RideCommentRequest;
import org.modsen.dto.response.PagedRatingResponse;
import org.modsen.dto.response.RateResponse;
import org.modsen.dto.response.RatingResponse;
import org.modsen.dto.response.SuccessResponse;
import org.modsen.service.RatingService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/ratings")
public class RatingController implements RatingApi {

    private final RatingService ratingService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public PagedRatingResponse getAllRatings(
        @RequestParam(defaultValue = "0") @Min(value = 0, message = "{page.incorrect}") int page,
        @RequestParam(defaultValue = "10") @Min(value = 1, message = "{limit.incorrect}") int limit,
        @RequestParam(defaultValue = "createdAt") String sortBy,
        @RequestParam(defaultValue = "asc") String sortDirection
    ) {
        RequestParams requestParams = RequestParams.builder()
            .page(page)
            .limit(limit)
            .sortBy(sortBy)
            .sortDirection(sortDirection)
            .build();
        return ratingService.getAllRatings(requestParams);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{participantId}", produces = APPLICATION_JSON_VALUE)
    public RateResponse getRateById(@PathVariable UUID participantId) {
        return ratingService.getRateById(participantId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/{fromId}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public SuccessResponse createRating(
        @Valid @RequestBody RatingRequest request,
        @PathVariable UUID fromId
    ) {
        UUID createdRatingId = ratingService.createRating(request, fromId);
        return new SuccessResponse(createdRatingId.toString());
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping(value = "/comments/{ratingId}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public RatingResponse addRideComment(@PathVariable UUID ratingId,
                                         @Valid @RequestBody RideCommentRequest request) {
        return ratingService.addRideComment(ratingId, request);
    }

}
