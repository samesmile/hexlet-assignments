package exercise;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import exercise.model.Post;

@SpringBootApplication
@RestController
public class Application {
    private final List<Post> posts = Data.getPosts();

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @GetMapping("/posts")
    public ResponseEntity<List<Post>> getAll(@RequestParam(defaultValue = "0") Integer page,
                             @RequestParam(defaultValue = "10") Integer limit) {
        int start = page * limit;
        int end = Math.min(start + limit, posts.size());

        if (start > posts.size()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ArrayList<>());
        }

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(posts.size()))
                .body(posts.subList(start, end));
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<Post> getById(@PathVariable String id) {
        var post = posts.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();

        return post.map(value -> ResponseEntity.ok()
                .body(value))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/posts")
    public ResponseEntity<Post> create(@RequestBody Post page) {
        posts.add(page);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(page);
    }

    @PutMapping("/posts/{id}")
    public ResponseEntity<Post> update(@PathVariable String id, @RequestBody Post data) {
        var postById = posts.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();

        if (postById.isPresent()) {
            var post = postById.get();
            post.setTitle(data.getTitle());
            post.setId(data.getId());
            post.setBody(data.getBody());

            return ResponseEntity.ok()
                    .body(post);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }

    @DeleteMapping("/posts/{id}")
    public void delete(@PathVariable String id) {
        posts.removeIf(p -> p.getId().equals(id));
    }
}
