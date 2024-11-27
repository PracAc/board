package org.oz.basic_board.board.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.oz.basic_board.board.domain.BoardEntity;
import org.springframework.boot.autoconfigure.web.format.DateTimeFormatters;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Data
@ToString
@NoArgsConstructor
public class BoardListDTO {
    private Long bno;
    private String title;
    private String writer;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime regDate;

    public BoardListDTO(Long bno, String title, String writer, LocalDateTime regDate) {
        this.bno = bno;
        this.title = title;
        this.writer = writer;
        this.regDate = regDate;
    }
}
