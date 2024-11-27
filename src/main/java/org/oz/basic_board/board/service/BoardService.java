package org.oz.basic_board.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.oz.basic_board.board.domain.BoardEntity;
import org.oz.basic_board.board.dto.*;
import org.oz.basic_board.board.repository.BoardEntityRepository;
import org.oz.basic_board.board.repository.ReviewEntityRepository;
import org.oz.basic_board.common.domain.AttachFile;
import org.oz.basic_board.common.dto.PageResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class BoardService {
    private final BoardEntityRepository boardEntityRepository;
    private final ReviewEntityRepository reviewEntityRepository;

    public PageResponseDTO<BoardListDTO> getList(BoardPageRequestDTO pageRequestDTO){
        log.info("------------------------------------getList");
        log.info(pageRequestDTO.toString());
        return boardEntityRepository.getListBoard(pageRequestDTO);
    }

    public Optional<BoardReadWithReviewDTO> getReadWithReview(Long bno){

        Optional<BoardEntity> result = boardEntityRepository.getBoard(bno);
        List<ReviewListDTO> reviewList = reviewEntityRepository.listByBno(bno);

        List<String> fileNames = result.get().getAttachFiles()
                .stream()
                .map(AttachFile::getFileName)
                .collect(Collectors.toList());

        return Optional.of(BoardReadWithReviewDTO.builder()
                .title(result.get().getTitle())
                .content(result.get().getContent())
                .writer(result.get().getWriter())
                .regDate(result.get().getRegDate())
                .attachFileNames(fileNames)
                .reviewList(reviewList)
                .build());
    }

    public Optional<BoardReadDTO> getReadOne(Long bno){
        Optional<BoardEntity> result = boardEntityRepository.getBoard(bno);

        List<String> fileNames = result.get().getAttachFiles()
                .stream()
                .map(AttachFile::getFileName)
                .collect(Collectors.toList());


        return Optional.of(BoardReadDTO.builder()
                .title(result.get().getTitle())
                .content(result.get().getContent())
                .writer(result.get().getWriter())
                .regDate(result.get().getRegDate())
                .attachFileNames(fileNames)
                .build());
    }

    public Long postAdd(BoardAddDTO boardAddDTO){

        log.info("===============================");
        log.info(boardAddDTO.toString());
        log.info("===============================");


        BoardEntity entity = BoardEntity.builder()
                .title(boardAddDTO.getTitle())
                .content(boardAddDTO.getContent())
                .writer(boardAddDTO.getWriter())
                .btype(boardAddDTO.getBtype())
                .build();

        log.info(boardAddDTO.getUploadFileNames());
        List<String> fileNames = boardAddDTO.getUploadFileNames();
        fileNames.forEach (fileName -> {
            entity.addFile(fileName);
        });

        boardEntityRepository.save(entity);

        return entity.getBno();
    }

    public Long putOne(BoardModifyDTO boardModifyDTO){
        log.info("=================MODIFIY==============");
        log.info(boardModifyDTO);

        BoardEntity boardEntity = boardEntityRepository.getOne(boardModifyDTO.getBno());

        boardEntity.changeTitle(boardModifyDTO.getTitle());
        boardEntity.changeContent(boardModifyDTO.getContent());

        boardEntity.removeFiles();

        boardModifyDTO.getAttachFileNames().forEach(fileName -> {
            boardEntity.addFile(fileName);
        });

        boardEntityRepository.save(boardEntity);

        return boardEntity.getBno();
    }

    public Long deleteOne(Long bno){
        BoardEntity boardEntity = boardEntityRepository.getOne(bno);

        boardEntity.changeDelFlag(true);
        boardEntityRepository.save(boardEntity);

        return boardEntity.getBno();
    }
}
