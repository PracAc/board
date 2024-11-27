package org.oz.basic_board.board.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.oz.basic_board.board.domain.BoardEntity;
import org.oz.basic_board.board.dto.*;
import org.oz.basic_board.common.domain.AttachFile;
import org.oz.basic_board.common.dto.PageResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@SpringBootTest
@Log4j2
public class BoardRepositoryTests {

    @Autowired
    private BoardEntityRepository boardEntityRepository;
    @Autowired
    private ReviewEntityRepository reviewEntityRepository;

    @Test
    public void insertBoard(){
        for (long i = 1; i < 150; i++) {

            BoardEntity boardEntity = BoardEntity.builder()
                    .title("Board " + i)
                    .content("Content " + i)
                    .writer("Writer " + i)
                    .btype(1)
                    .build();
            boardEntity.addFile(UUID.randomUUID()+"test.jpg");
            boardEntity.addFile(UUID.randomUUID()+"test.jpg");

            boardEntityRepository.save(boardEntity);

        }//end for

    }

    @Test
    public void testList() {

        BoardPageRequestDTO pageRequestDTO = new BoardPageRequestDTO();
        pageRequestDTO.setBtype(1);
        pageRequestDTO.setPage(1);
        pageRequestDTO.setSize(10);


        PageResponseDTO<BoardListDTO> response = boardEntityRepository.getListBoard(pageRequestDTO);

        log.info(response);
    }

    @Test
    public void testRead(){
        Long bno = 149l;

        Optional<BoardEntity> result = boardEntityRepository.getBoard(bno);
        List<String> fileNames = result.get().getAttachFiles()
                .stream()
                .map(AttachFile::getFileName)
                .collect(Collectors.toList());

        List<ReviewListDTO> reviewList = reviewEntityRepository.listByBno(bno);

       BoardReadWithReviewDTO dto = BoardReadWithReviewDTO.builder()
                .title(result.get().getTitle())
                .content(result.get().getContent())
                .writer(result.get().getWriter())
                .regDate(result.get().getRegDate())
                .attachFileNames(fileNames)
                .reviewList(reviewList)
                .build();
        log.info(result.get().toString());
        log.info(dto);
    }

    @Test
    @Transactional
    @Commit
    public void testModify(){
        log.info("=================MODIFIY==============");
        List<String> fileNames = new ArrayList<>();
        fileNames.add("aaa.jpg");
        fileNames.add("bbb.jpg");
        fileNames.add("ccc.jpg");

        BoardModifyDTO boardModifyDTO = BoardModifyDTO.builder()
                .bno(149L)
                .title("title")
                .content("content")
                .attachFileNames(fileNames)
                .build();

        BoardEntity boardEntity = boardEntityRepository.getOne(boardModifyDTO.getBno());

        boardEntity.removeFiles();

        boardModifyDTO.getAttachFileNames().forEach(fileName -> {
            boardEntity.addFile(fileName);
        });

        boardEntityRepository.save(boardEntity);
    }

}
