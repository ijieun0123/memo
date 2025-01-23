package com.example.memo.controller;

import com.example.memo.dto.MemoRequestDto;
import com.example.memo.dto.MemoResponseDto;
import com.example.memo.entity.Memo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/memos")
public class MemoController {

    private final Map<Long, Memo> memoList = new HashMap<>();

    // 메모 생성
    @PostMapping
    public ResponseEntity<MemoResponseDto> createMemo(@RequestBody MemoRequestDto dto){

        // 식별자가 1씩 증가하도록 만듦
        Long memoId = memoList.isEmpty() ? 1 : Collections.max(memoList.keySet()) + 1;

        // 요청받은 데이터로 Memo 객체 생성
        Memo memo = new Memo(memoId, dto.getTitle(), dto.getContents());

        // Inmemory DB 에 Memo 저장
        memoList.put(memoId, memo);

        return new ResponseEntity<>(new MemoResponseDto(memo), HttpStatus.CREATED);
    }

    // 메모 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<MemoResponseDto> findMemoById(@PathVariable Long id){

        Memo memo = memoList.get(id);

        if(memo == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(new MemoResponseDto(memo), HttpStatus.OK);
    }

    // 메모 전체 조회
    @GetMapping
    public ResponseEntity<List<MemoResponseDto>> findAllMemos(){

        // init List
        List<MemoResponseDto> responseList = new ArrayList<>();

        // HashMap<Memo> => List<MemoResponseDto>
        for(Memo memo : memoList.values()){
            MemoResponseDto responseDto = new MemoResponseDto(memo);
            responseList.add(responseDto);
        }

        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    // 메모 단건 전체 수정
    @PutMapping("/{id}")
    public ResponseEntity<MemoResponseDto> updateMemoById(
            @PathVariable Long id,
            @RequestBody MemoRequestDto dto
    ){
        Memo memo = memoList.get(id);

        if(memo == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if(dto.getTitle() == null || dto.getContents() == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        memo.update(dto);

        return new ResponseEntity<>(new MemoResponseDto(memo), HttpStatus.OK);
    }

    // 메모 단건 제목 수정
    @PatchMapping("/{id}")
    public ResponseEntity<MemoResponseDto> updateTitle(
            @PathVariable Long id,
            @RequestBody MemoRequestDto dto
    ){
        Memo memo = memoList.get(id);

        // NPE 방지
        if(memo == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if(dto.getTitle() == null || dto.getContents() != null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        memo.updateTitle(dto);

        return new ResponseEntity<>(new MemoResponseDto(memo), HttpStatus.OK);
    }

    // 메모 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMemo(@PathVariable Long id){

        // memoList 의 key 값에 id 를 포함하고 있다면 삭제
        if(memoList.containsKey(id)) {
            memoList.remove(id);

            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
