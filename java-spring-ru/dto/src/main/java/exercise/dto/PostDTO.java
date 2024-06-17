package exercise.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PostDTO {

    private long id;
    private String title;
    private String body;
    private List<CommentDTO> comments;
}