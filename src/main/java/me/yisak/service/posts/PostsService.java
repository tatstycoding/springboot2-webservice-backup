package me.yisak.service.posts;

import lombok.RequiredArgsConstructor;
import me.yisak.domain.posts.Posts;
import me.yisak.domain.posts.PostsRepository;
import me.yisak.web.dto.PostsListResponseDto;
import me.yisak.web.dto.PostsResponseDto;
import me.yisak.web.dto.PostsSaveRequestDto;
import me.yisak.web.dto.PostsUpdateRequestDto;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;
import java.util.List;

@RequiredArgsConstructor //final이 선언된 모든 필드를 인자값으로 하는 생성자를 이 롬복 애너테이션이 대신 생성해줌.
@Service
public class PostsService {

    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
        return postsRepository.save(requestDto.toEntity()).getId();
    }


    // update기능에서 DB에 쿼리를 날리는 부분이 없다. JPA의 영속성 컨텍스트 때문이다.(엔티티를 영구 저장하는 환경)
    // 트랜잭션 안에서 DB에서 데이터를 가져오면 이 데이터는 영속성 컨텍스트가 유지된 상태.
    // 그 상태에서 해당 데이터의 값을 변경하면 트랜잭션이 끝나는 시점에 해당 테이블에 변경분을 반영..
    // 고로 Entity 객체의 값만 변경하면 별도로 Update쿼리를 날릴 필요가 없다. 이 개념을 더티 체킹이라고 함.
    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id=" + id));

        posts.update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }

    @Transactional(readOnly = true)
    public PostsResponseDto findById(Long id) {
        Posts entity = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id=" + id));

        return new PostsResponseDto(entity);
    }


     // 트랜잭션 범위는 유지하되, 조회 기능만 남겨두어 조회 속도가 개선
     // 고로 등록, 수정, 삭제 기능이 전혀 없는 서비스 메소드에 사용하는 것 추천.
     @Transactional(readOnly = true)
    public List<PostsListResponseDto> findAllDesc(){
        return postsRepository.findAllDesc().stream()  // 1. postsRepository 결과로 넘어온 Posts의 Stream을
                .map(PostsListResponseDto::new)        // 2. map을 통해 PostsListsResponseDto 변환
              //.map(posts-> new PostsListReponseDto(posts))
                .collect(Collectors.toList());         // 3. -> List로 반환하는 메소드
    }

    @Transactional
    public void delete (Long id){
        Posts posts = postsRepository.findById(id)  // 존재하는 Posts인지 확인을 위해 엔티티 조회
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id=" + id));

        postsRepository.delete(posts);  //JpaRepository에서 이미 delete메소드를 지원하고 있다.
                                        // 엔티티를 파라미터로 삭제할 수도 있고, deleteById메소드를 이용하면 id로 삭제할 수도 있다.
                                        // 삭제.
    }


}
