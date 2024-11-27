package org.oz.basic_board.board.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.oz.basic_board.common.dto.PageRequestDTO;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class BoardPageRequestDTO extends PageRequestDTO {

    private Integer btype;
}
