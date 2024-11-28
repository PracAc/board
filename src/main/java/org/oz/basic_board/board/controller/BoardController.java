package org.oz.basic_board.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.oz.basic_board.board.dto.*;
import org.oz.basic_board.board.service.BoardService;
import org.oz.basic_board.common.dto.PageResponseDTO;
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

    @GetMapping("/list")
    public ResponseEntity<PageResponseDTO<BoardListDTO>> boardList(BoardPageRequestDTO pageRequestDTO) {
        return ResponseEntity.ok().body(boardService.getList(pageRequestDTO));
    }

    @GetMapping("/read/{bno}")
    public ResponseEntity<BoardReadWithReviewDTO> readBoard(@PathVariable("bno") Long bno) {

        return ResponseEntity.ok().body(boardService.getReadWithReview(bno).get());
    }

    @PostMapping("")
    public ResponseEntity<Long> postBoard(BoardAddDTO boardAddDTO, @RequestPart(required = false) MultipartFile[] files) {

        // nginx 사용시 업로드처리하며 받아올 UUID로 생성된 이름 임의로 생성
        List<String> fileNames = new ArrayList<>();

        if (files != null && files.length > 0) {
            for (MultipartFile file : files) {
                fileNames.add(UUID.randomUUID() + "_" + file.getOriginalFilename());
            }
        }

        boardAddDTO.setUploadFileNames(fileNames);

        return ResponseEntity.ok().body(boardService.addBoard(boardAddDTO).getAsLong());
    }

    @PutMapping("/edit/{bno}")
    public ResponseEntity<Long> putBoard(@PathVariable("bno") Long bno, BoardModifyDTO boardModifyDTO,
                                         @RequestPart(required = false) MultipartFile[] files) {

        log.info("---------------------------EDIT ----------------------------------------");
        // nginx 사용시 업로드처리하며 받아올 UUID로 생성된 이름 임의로 생성
        List<String> fileNames = new ArrayList<>();

        if (files != null && files.length > 0) {
            for (MultipartFile file : files) {
                fileNames.add(UUID.randomUUID() + "_" + file.getOriginalFilename());
            }
        }

        boardModifyDTO.setBno(bno);
        boardModifyDTO.setAttachFileNames(fileNames);

        return ResponseEntity.ok().body(boardService.modifyBoard(boardModifyDTO).getAsLong());
    }

    @PutMapping("/delete/{bno}")
    public ResponseEntity<Long> deleteBoard(@PathVariable("bno") Long bno) {
        return ResponseEntity.ok().body(boardService.deleteBoard(bno).getAsLong());
    }

}
