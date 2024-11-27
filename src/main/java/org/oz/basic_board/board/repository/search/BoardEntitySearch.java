package org.oz.basic_board.board.repository.search;

import org.oz.basic_board.board.dto.BoardListDTO;
import org.oz.basic_board.board.dto.BoardPageRequestDTO;
import org.oz.basic_board.common.dto.PageResponseDTO;

public interface BoardEntitySearch{
    PageResponseDTO<BoardListDTO> getListBoard(BoardPageRequestDTO pageRequestDTO);
}
