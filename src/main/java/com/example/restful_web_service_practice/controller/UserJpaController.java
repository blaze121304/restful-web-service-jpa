package com.example.restful_web_service_practice.controller;

import com.example.restful_web_service_practice.common.exception.UserNotFoundException;
import com.example.restful_web_service_practice.repository.PostRepository;
import com.example.restful_web_service_practice.repository.UserRepository;
import com.example.restful_web_service_practice.dto.Post;
import com.example.restful_web_service_practice.dto.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/jpa")
public class UserJpaController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    //http://localhost:8088/jpa/users or http://localhost:8088/users
    @GetMapping("/users")
    public List<User> retrieveAllUsers(){
        return userRepository.findAll();
    }

    @GetMapping("/users/{id}")
    public EntityModel<User> retrieveUser(@PathVariable int id){
        Optional<User> user = userRepository.findById(id);

        if(!user.isPresent()){
            throw new UserNotFoundException(String.format("ID[%s] not found",id));
        }

        //Spring boot 3.1 Hateoas -> Resource -> EntityModel
        EntityModel<User> userEntityModel = EntityModel.of(user.get());
        WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllUsers());
        userEntityModel.add(linkTo.withRel("all-users"));

        return userEntityModel;
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int id){

        Optional<User> user = userRepository.findById(id);

        if(!user.isPresent()){
            throw new UserNotFoundException(String.format("ID[%s] not found",id));
        }

        userRepository.deleteById(id);
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user){
        User savedUser = userRepository.save(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();

        return ResponseEntity.created(location).build();

    }

    // /jpa/users/90001/posts
    @GetMapping("/users/{id}/posts")
    public List<Post> retrieveAllPostsByUser(@PathVariable int id) {
        Optional<User> user = userRepository.findById(id);

        if(!user.isPresent()){
            throw new UserNotFoundException(String.format("ID[%s] not found",id));
        }

        if(user.get().getPosts().isEmpty()){
            throw new UserNotFoundException(String.format("ID[%s] Posts not found",id));
        }

        return user.get().getPosts();


    }

    @PostMapping("/users/{id}/posts")
    public ResponseEntity<Post> createPost(@PathVariable int id, @RequestBody Post post){

        //사용자 검색
        Optional<User> user = userRepository.findById(id);

        if(!user.isPresent()){
            throw new UserNotFoundException(String.format("ID[%s] not found",id));
        }

        //포스트할 유저를 추가 - 2. 불변성 위배
        post.setUser(user.get());


        //포스트
        Post savedPost = postRepository.save(post);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedPost.getId())
                .toUri();

        return ResponseEntity.created(location).build();

    }

    //@DeleteMapping("/users/{id}/{post_id}")
    @DeleteMapping("/users/{id}/posts/{post_id}")
    public void deletePost(@PathVariable int id, @PathVariable int post_id){

        //사용자 검색 - 사용자 id/포스트 id를 사용자가 입력하자나...
        Optional<User> user = userRepository.findById(id);
        Optional<Post> post = postRepository.findById(post_id);

        //1. 사용자가 검색이 안되는 경우    //2. 사용자 검색은 되는데 포스트가 없을 경우
        if(!user.isPresent()){
            throw new UserNotFoundException(String.format("ID[%s] not found",id));
        }else if(!post.isPresent()){
            throw new UserNotFoundException(String.format("ID[%s]'s post number %s is not found",id,post_id));
        }

        //3. 사용자 검색은 되는데 포스트가 있는 경우 - 삭제
        postRepository.deleteById(post_id);

        //이게 훨씬 좋음
//        Optional<User> user = userRepository.findById(id);
//
//        if (!user.isPresent()) {
//            throw new UserNotFoundException(String.format("ID[%s} not found", id));
//        } else {
//            postRepository.deleteById(post_id);
//        }


    }


}
