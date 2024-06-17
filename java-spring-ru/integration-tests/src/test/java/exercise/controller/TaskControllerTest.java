package exercise.controller;

import org.junit.jupiter.api.Test;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import org.instancio.Instancio;
import org.instancio.Select;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import java.time.LocalDate;
import java.util.HashMap;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import exercise.repository.TaskRepository;
import exercise.model.Task;

@SpringBootTest
@AutoConfigureMockMvc
class ApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Faker faker;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TaskRepository taskRepository;


    @Test
    public void testWelcomePage() throws Exception {
        var result = mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThat(body).contains("Welcome to Spring!");
    }

    @Test
    public void testIndex() throws Exception {
        var result = mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }


    @Test
    public void testFindById() throws Exception {
        var task = new Task();
        task.setDescription(faker.lorem().paragraph());
        task.setTitle(faker.lorem().word());
        task.setCreatedAt(LocalDate.now().minusDays(2));
        task.setUpdatedAt(LocalDate.now().minusDays(1));

        taskRepository.save(task);

        var result = mockMvc.perform(get("/tasks/" + taskRepository.findByTitle(task.getTitle()).get().getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThatJson(result).isObject().containsEntry("title", task.getTitle());
        assertThatJson(result).isObject().containsEntry("description", task.getDescription());
        assertThatJson(result).isObject().containsEntry("updatedAt", task.getUpdatedAt().toString());
        assertThatJson(result).isObject().containsEntry("createdAt", task.getCreatedAt().toString());
    }

    @Test
    public void testCreate() throws Exception {
        var task = new Task();
        task.setDescription(faker.lorem().paragraph());
        task.setTitle(faker.lorem().word());

        var result = mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(om.writeValueAsString(task)))
                .andExpect(status().is(201))
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThatJson(result).isObject().containsEntry("title", task.getTitle());
        assertThatJson(result).isObject().containsEntry("description", task.getDescription());
        assertThatJson(result).isObject().containsEntry("updatedAt", LocalDate.now().toString());
        assertThatJson(result).isObject().containsEntry("createdAt", LocalDate.now().toString());
    }

    @Test
    public void testUpdate() throws Exception {
        var task = new Task();
        task.setDescription(faker.lorem().paragraph());
        task.setTitle(faker.lorem().word());
        task.setCreatedAt(LocalDate.now().minusDays(2));
        task.setUpdatedAt(LocalDate.now().minusDays(1));

        taskRepository.save(task);

        task.setDescription(faker.lorem().paragraph());

        var result = mockMvc.perform(put("/tasks/" + taskRepository.findByTitle(task.getTitle()).get().getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(om.writeValueAsString(task)))
                .andExpect(status().is(200))
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThatJson(result).isObject().containsEntry("title", task.getTitle());
        assertThatJson(result).isObject().containsEntry("description", task.getDescription());
        assertThatJson(result).isObject().containsEntry("updatedAt", LocalDate.now().toString());
        assertThatJson(result).isObject().containsEntry("createdAt", LocalDate.now().toString());
    }

    @Test
    public void testDelete() throws Exception {
        var task = new Task();
        task.setDescription(faker.lorem().paragraph());
        task.setTitle(faker.lorem().word());
        task.setCreatedAt(LocalDate.now().minusDays(2));
        task.setUpdatedAt(LocalDate.now().minusDays(1));

        taskRepository.save(task);

        var result = mockMvc.perform(delete("/tasks/" + taskRepository.findByTitle(task.getTitle()).get().getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(om.writeValueAsString(task)))
                .andExpect(status().is(200))
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(taskRepository.findByTitle(task.getTitle()).isPresent()).isFalse();
    }
}
