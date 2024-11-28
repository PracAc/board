package org.oz.basic_board.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.oz.basic_board.board.dto.*;
import org.oz.basic_board.board.service.BoardService;
import org.oz.basic_board.common.dto.PageResponseDTO;
import org.oz.basic_board.utill.CustomFileUtil;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
@Log4j2
public class BoardController {
    private final BoardService boardService;
    private final CustomFileUtil fileUtil;

    @GetMapping("/list")
    public ResponseEntity<PageResponseDTO<BoardListDTO>> boardList(BoardPageRequestDTO pageRequestDTO) {
        return ResponseEntity.ok().body(boardService.getList(pageRequestDTO));
    }

    @GetMapping("/read/{bno}")
    public ResponseEntity<BoardReadWithReviewDTO> readBoard(@PathVariable("bno") Long bno) {

        return ResponseEntity.ok().body(boardService.getReadWithReview(bno).get());
    }

    @PostMapping("")
    public ResponseEntity<Long> postBoard(BoardAddDTO boardAddDTO) {

        List<String> uploadFileNames = fileUtil.saveFiles(boardAddDTO.getFiles());

        boardAddDTO.setUploadFileNames(uploadFileNames);
        return ResponseEntity.ok().body(boardService.addBoard(boardAddDTO).getAsLong());
    }

    @PutMapping("/edit/{bno}")
    public ResponseEntity<Long> putBoard(@PathVariable("bno") Long bno, BoardModifyDTO boardModifyDTO) {

        log.info("---------------------------EDIT ----------------------------------------");

        log.info("bno: " + bno);
        log.info("boardModifyDTO: " + boardModifyDTO);
        List<String> uploadFileNames = fileUtil.saveFiles(boardModifyDTO.getFiles());

        uploadFileNames.forEach(fileName -> boardModifyDTO.getAttachFileNames().add(fileName));
        fileUtil.deleteFiles(boardModifyDTO.getDeleteFileNames());

        return ResponseEntity.ok().body(boardService.modifyBoard(boardModifyDTO).getAsLong());
//        return null;
    }

    @GetMapping("/img/{fileName}")
    public ResponseEntity<Resource> getImg(@PathVariable String fileName) {
        return fileUtil.getFile(fileName);
    }

    @PutMapping("/delete/{bno}")
    public ResponseEntity<Long> deleteBoard(@PathVariable("bno") Long bno) {
        return ResponseEntity.ok().body(boardService.deleteBoard(bno).getAsLong());
    }

}
