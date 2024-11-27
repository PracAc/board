package org.oz.basic_board.board.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@ToString
public class ReviewListDTO {
    private String reviewer;
    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime regDate;
}
