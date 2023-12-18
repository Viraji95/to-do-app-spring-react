package lk.ijse.dep11.app.api;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lk.ijse.dep11.app.to.TaskTO;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PreDestroy;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@CrossOrigin
public class TaskHttpController {

    private final HikariDataSource pool;

    public TaskHttpController(Environment env) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(env.getRequiredProperty("spring.datasource.url"));
        config.setUsername(env.getRequiredProperty("spring.datasource.username"));
        config.setPassword(env.getRequiredProperty("spring.datasource.password"));
        config.setDriverClassName(env.getRequiredProperty("spring.datasource.driver-class-name"));
        config.setMaximumPoolSize(env.getRequiredProperty("spring.datasource.hikari.maximum-pool-size", Integer.class));
        pool = new HikariDataSource(config);

    }


    @PreDestroy
    public void destroy() {
        pool.close();
    }
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(produces = "application/json", consumes = "application/json")
    public TaskTO createTask(@RequestBody @Validated (TaskTO.Create.class) TaskTO task){
        try(Connection connection = pool.getConnection()){
            PreparedStatement stm = connection.prepareStatement("INSERT INTO tasks (description, status, email) VALUES (?, FALSE, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            stm.setString(1, task.getDescription());
            stm.setString(2, task.getEmail());
            stm.executeUpdate();
            ResultSet generatedKeys = stm.getGeneratedKeys();
            generatedKeys.next();
            int id = generatedKeys.getInt(1);
            task.setId(id);
            task.setStatus(false);
            return task;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    @GetMapping(produces ="application/json", params = {"email"})
    public List<TaskTO> getAllTasks(String email) {
        try(Connection connection = pool.getConnection()){
            PreparedStatement stm = connection.prepareStatement("SELECT * FROM tasks WHERE email = ? ORDER BY id");
            stm.setString(1, email);
            ResultSet rst = stm.executeQuery();
            LinkedList<TaskTO> taskList = new LinkedList<>();
            while (rst.next()){
                int id = rst.getInt("id");
                String description = rst.getString("description");
                boolean status = rst.getBoolean("status");
                taskList.add(new TaskTO(id, description,status, email));

            }
            return taskList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable int id) {
        try(Connection connection = pool.getConnection()){
            PreparedStatement stmExists = connection.prepareStatement("SELECT * FROM tasks WHERE id = ?");
            stmExists.setInt(1, id);
            if(!stmExists.executeQuery().next()){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task Not Found");
            }

            PreparedStatement stm = connection.prepareStatement("DELETE FROM tasks WHERE id = ?");
            stm.setInt(1, id);
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(value = "/{id}", consumes = "application/json")
    public void updateTask(@PathVariable("id") int taskId,
                           @RequestBody @Validated(TaskTO.Update.class) TaskTO task) {
        try(Connection connection = pool.getConnection()){
            PreparedStatement stmExist = connection.prepareStatement("SELECT * FROM tasks WHERE id = ?");
            stmExist.setInt(1, taskId);
            if(!stmExist.executeQuery().next()){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Task Not Found");
            }
            PreparedStatement stm = connection.prepareStatement("UPDATE tasks SET description = ?, status = ? WHERE id = ?");
            stm.setString(1, task.getDescription());
            stm.setBoolean(2, task.getStatus());
            stm.setInt(3,task.getId());
            stm.executeUpdate();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
