package org.oz.basic_board.board.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.oz.basic_board.board.domain.BoardEntity;
import org.oz.basic_board.board.domain.ReviewEntity;
import org.oz.basic_board.board.dto.ReviewListDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Log4j2
public class ReviewRepositoryTests {
    @Autowired
    private ReviewEntityRepository entityRepository;

    @Test
    public void insertReview() {
        BoardEntity boardEntity = BoardEntity.builder().bno(149L).build();
        for (int i = 0; i < 3; i++) {
            ReviewEntity entity = ReviewEntity.builder()
                    .board(boardEntity)
                    .reviewer("Reviewer" + i)
                    .content("Content" + i)
                    .build();
            entityRepository.save(entity);
        }
    }

    @Test
    public void TestList(){
        Long bno = 149L;

        List<ReviewListDTO> list = entityRepository.listByBno(bno);

        list.forEach(result -> log.info(result.toString()));
    }
}
