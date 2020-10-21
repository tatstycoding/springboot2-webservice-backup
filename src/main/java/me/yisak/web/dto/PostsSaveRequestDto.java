package me.yisak.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.yisak.domain.posts.Posts;


@Getter
@NoArgsConstructor
public class PostsSaveRequestDto {
// Request & Response Dto 클래스입니다 (view Layer).

    private String title;
    private String content;
    private String author;

    @Builder
    public PostsSaveRequestDto(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public Posts toEntity(){
        return Posts.builder()
                        .title(title)
                        .content(content)
                        .author(author)
                        .build();
    }
}
