package org.oz.basic_board.board.repository;

import org.oz.basic_board.board.domain.ReviewEntity;
import org.oz.basic_board.board.repository.search.ReviewEntitySearch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewEntityRepository extends JpaRepository<ReviewEntity,Long>, ReviewEntitySearch {
}
