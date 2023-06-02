package in.bushansirgur.springbootmongodb.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.bushansirgur.springbootmongodb.exception.TodoCollectionException;
import in.bushansirgur.springbootmongodb.model.TodoDTO;
import in.bushansirgur.springbootmongodb.repository.TodoRepository;

@Service
public class TodoServiceImpl implements TodoService{

    @Autowired
    private TodoRepository todoRepo;

    @Override
    public void createTodo(TodoDTO todo) throws ConstraintViolationException, TodoCollectionException{
        Optional<TodoDTO> todoOptional = todoRepo.findByTodo(todo.getTodo());
        if(todoOptional.isPresent()){
            throw new TodoCollectionException(TodoCollectionException.TodoAlreadyExists());
        } else {
            todo.setCreatedAt(new Date(System.currentTimeMillis()));
            todoRepo.save(todo);
        }
    }

    public List<TodoDTO> getAllTodos() {
        List<TodoDTO> todos = todoRepo.findAll();
        if(todos.size() > 0) {
            return todos;
        } else {
            return new ArrayList<TodoDTO>();
        }
    }

    public TodoDTO getSingleTodo(String id) throws TodoCollectionException {
        Optional<TodoDTO> optionalTodo = todoRepo.findById(id);
        if(!optionalTodo.isPresent()){
            throw new TodoCollectionException(TodoCollectionException.NotFoundException(id));
        }else {
            return optionalTodo.get();
        }
    }

    public void updateTodo(String id, TodoDTO todo) throws TodoCollectionException{
        Optional<TodoDTO> todoWithId = todoRepo.findById(id);
        Optional<TodoDTO> todoWtihSameName = todoRepo.findByTodo(todo.getTodo());
        if(todoWithId.isPresent()){

            if(todoWtihSameName.isPresent() && !todoWtihSameName.get().getId().equals(id)){
                throw new TodoCollectionException(TodoCollectionException.TodoAlreadyExists());

            }


            TodoDTO todoToUpdate = todoWithId.get();

            todoToUpdate.setTodo(todo.getTodo());
            todoToUpdate.setDescription(todo.getDescription());
            todoToUpdate.setCompleted(todo.getCompleted());
            todoToUpdate.setUpdatedAt(new Date(System.currentTimeMillis()));
            todoRepo.save(todoToUpdate);
        }else {
            throw new TodoCollectionException(TodoCollectionException.NotFoundException(id));
        }
    }

    @Override
    public void deleteTodoById(String id) throws TodoCollectionException {
        Optional<TodoDTO> todoOptional = todoRepo.findById(id);
        if(!todoOptional.isPresent()){
            throw new TodoCollectionException(TodoCollectionException.NotFoundException(id));
        } else {
            todoRepo.deleteById(id);
        }
    }
}
