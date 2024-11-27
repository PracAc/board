package org.oz.basic_board.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardModifyDTO {
    private Long bno;
    private String title;
    private String content;

    private List<MultipartFile> Files;
    private List<String> attachFileNames;
}
