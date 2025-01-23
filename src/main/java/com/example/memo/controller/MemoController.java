package com.example.memo.controller;

import com.example.memo.dto.MemoRequestDto;
import com.example.memo.dto.MemoResponseDto;
import com.example.memo.entity.Memo;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/memos")
public class MemoController {

    private final Map<Long, Memo> memoList = new HashMap<>();

    // 메모 생성
    @PostMapping
    public MemoResponseDto createMemo(@RequestBody MemoRequestDto dto){

        // 식별자가 1씩 증가하도록 만듦
        Long memoId = memoList.isEmpty() ? 1 : Collections.max(memoList.keySet()) + 1;

        // 요청받은 데이터로 Memo 객체 생성
        Memo memo = new Memo(memoId, dto.getTitle(), dto.getContents());

        // Inmemory DB 에 Memo 저장
        memoList.put(memoId, memo);

        return new MemoResponseDto(memo);
    }

    // 메모 조회
    @GetMapping("/{id}")
    public MemoResponseDto findMemoById(@PathVariable Long id){

        Memo memo = memoList.get(id);

        return new MemoResponseDto(memo);
    }

    // 메모 단건 전체 수정
    @PutMapping("/{id}")
    public MemoResponseDto updateMemoById(
            @PathVariable Long id,
            @RequestBody MemoRequestDto dto
    ){
        Memo memo = memoList.get(id);

        memo.update(dto);

        return new MemoResponseDto(memo);
    }

    // 메모 삭제
    @DeleteMapping("/{id}")
    public void deleteMemo(@PathVariable Long id){

        memoList.remove(id);

    }
}
