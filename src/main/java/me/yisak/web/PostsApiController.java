package me.yisak.web;

import lombok.RequiredArgsConstructor;
import me.yisak.service.posts.PostsService;
import me.yisak.web.dto.PostsResponseDto;
import me.yisak.web.dto.PostsSaveRequestDto;
import me.yisak.web.dto.PostsUpdateRequestDto;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class PostsApiController {

    private final PostsService postsService;

    @PostMapping("/api/v1/posts") // 생성 POST
    public Long save(@RequestBody PostsSaveRequestDto requestDto){ //@RequestBody : HTTP 요청의 body 내용을 자바 객체로 매핑하는 역할을 합니다.
        return postsService.save(requestDto);
    }

    //게시글 수정,삭제 화면.
    @PutMapping("/api/v1/posts/{id}") // 수정 PUT
    public Long update(@PathVariable Long id,@RequestBody PostsUpdateRequestDto requestDto){
        return postsService.update(id, requestDto);
    }

    @GetMapping("/api/v1/posts/{id}") // 읽기 GET
    public PostsResponseDto findById (@PathVariable Long id) {
        return postsService.findById(id);
    }

    @DeleteMapping("/api/v1/posts/{id}") // 삭제 Delete
    public Long delete(@PathVariable Long id){
        postsService.delete(id);
        return id;
    }




}
