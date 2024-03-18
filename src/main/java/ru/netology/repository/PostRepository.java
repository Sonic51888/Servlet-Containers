package ru.netology.repository;

import ru.netology.model.Post;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class PostRepository {
    private volatile ConcurrentHashMap<Long, Post> postCollection = new ConcurrentHashMap<Long, Post>();
    AtomicLong countPosts = new AtomicLong(0L);

    public PostRepository() {
        this.postCollection = new ConcurrentHashMap<>();
    }

    public List<Post> all() {
        return postCollection.values().parallelStream().collect(Collectors.toList());
    }

    public Optional<Post> getById(long id) {
        return Optional.ofNullable(postCollection.get(id));
    }

    public Post save(Post post) {
        if (post.getId() == 0) {
            countPosts.incrementAndGet();
            post.setId(countPosts.get());
            postCollection.put(countPosts.get(), post);
            return postCollection.get(countPosts.get());
        } else {
            Post postByIdFromCollection = postCollection.get(post.getId());
            postByIdFromCollection.setContent(post.getContent());
            return postByIdFromCollection;
        }
    }

    public void removeById(long id) {
    }
}
