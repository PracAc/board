package org.oz.basic_board.board.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@Builder
@ToString
public class ReviewListDTO {
    private Long rno;
    private String reviewer;
    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime regDate;
}
