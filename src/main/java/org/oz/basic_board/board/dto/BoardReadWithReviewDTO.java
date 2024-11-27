package org.oz.basic_board.board.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@ToString
@SuperBuilder
@Getter
@Setter
public class BoardReadWithReviewDTO extends BoardReadDTO {
    private List<ReviewListDTO> reviewList;
}
