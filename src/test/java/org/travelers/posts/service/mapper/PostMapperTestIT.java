package org.travelers.posts.service.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.travelers.posts.PostsApp;
import org.travelers.posts.service.dto.PostDTO;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.travelers.posts.util.TestUtil.convertObjectToJson;
import static org.travelers.posts.util.TestUtil.getPostDTO;

@SpringBootTest(classes = PostsApp.class)
public class PostMapperTestIT {

    @Autowired
    private PostMapper mapper;

    @Test
    void mapToPost() throws IOException {
        PostDTO postDTO = getPostDTO();
        String json = convertObjectToJson(postDTO);

        PostDTO postDTO2 = mapper.mapToPost(json);

        assertThat(postDTO2.getLogin()).isEqualTo(postDTO.getLogin());
    }

    @Test
    void mapToJson() throws IOException {
        PostDTO postDTO = getPostDTO();

        String json2 = mapper.mapToJson(postDTO);

        assertThat(json2).isNotNull();
        assertThat(json2).isNotEmpty();
        assertThat(json2).isNotBlank();
    }

}
