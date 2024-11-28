package org.oz.basic_board.board.repository;

import org.oz.basic_board.board.domain.BoardEntity;
import org.oz.basic_board.board.repository.search.BoardEntitySearch;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BoardEntityRepository extends JpaRepository<BoardEntity,Long>, BoardEntitySearch {

//    @EntityGraph(attributePaths = {"attachFiles"})
    @Query("""
    SELECT b FROM BoardEntity b
    JOIN FETCH b.attachFiles af
    WHERE b.bno = :bno
    ORDER BY af.ord ASC
""")
    Optional<BoardEntity> getBoard(@Param("bno") Long bno);

}
