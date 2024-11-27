package org.oz.basic_board.board.repository.search;

import org.oz.basic_board.board.dto.ReviewListDTO;

import java.util.List;

public interface ReviewEntitySearch {

    List<ReviewListDTO> listByBno(Long bno);
}
