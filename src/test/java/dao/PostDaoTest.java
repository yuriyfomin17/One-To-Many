package dao;

import model.Comment;
import model.Post;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import util.EntityManagerUtil;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class PostDaoTest {
    private static EntityManagerUtil emUtil;

    private static PostDao postDao;

    private static EntityManagerFactory entityManagerFactory;

    @BeforeAll
    static void setup(){
        entityManagerFactory = Persistence.createEntityManagerFactory("One-To-Many");
        emUtil = new EntityManagerUtil(entityManagerFactory);
        postDao = new PostDaoImpl(entityManagerFactory);
    }

    @AfterAll
    static void destroy(){ entityManagerFactory.close(); }

    @Test
    void testSavePost(){
        Post post = new Post();
        post.setName("Hibernate Master Class");

        Comment comment1 = new Comment();
        comment1.setReview("Nice Post!");
        Comment comment2 = new Comment();
        comment2.setReview("Good Post!");

        post.addComment(comment1);
        post.addComment(comment2);

        postDao.savePost(post);
    }

    @Test
    void testSavePostUpdateComment(){
        Post post = new Post();
        post.setName("Hibernate Master Class");

        Comment comment1 = new Comment();
        comment1.setReview("Nice Post!");
        Comment comment2 = new Comment();
        comment2.setReview("Good Post!");

        post.addComment(comment1);
        post.addComment(comment2);
        postDao.savePost(post);
        assertThat(post.getId(), notNullValue());
//        Post managedPost = emUtil.performReturningWithinTx(entityManager -> entityManager.find(Post.class, post.getId()));
        post.getComments()
                .stream()
                .filter(comment -> comment.getReview().toLowerCase().contains("nice"))
                .findAny()
                .ifPresent(comment -> comment.setReview("Keep Up good work"));

        Post updatedPost = postDao.updatePost(post);
        System.out.println(updatedPost);
        updatedPost.getComments().forEach(System.out::println);

    }

    @Test
    void testDeltePost(){
        Post post = new Post();
        post.setName("Hibernate Master Class");

        Comment comment1 = new Comment();
        comment1.setReview("Nice Post!");
        Comment comment2 = new Comment();
        comment2.setReview("Good Post!");

        post.addComment(comment1);
        post.addComment(comment2);
        postDao.savePost(post);
        assertThat(post.getId(), notNullValue());

        postDao.delete(post.getId());

    }

    @Test
    void deleteOneComment(){
        Post post = new Post();
        post.setName("Hibernate Master Class");

        Comment comment1 = new Comment();
        comment1.setReview("Nice Post!");
        Comment comment2 = new Comment();
        comment2.setReview("Good Post!");

        post.addComment(comment1);
        post.addComment(comment2);
        postDao.savePost(post);
        assertThat(post.getId(), notNullValue());

        Post managedPost = emUtil.performReturningWithinTx(entityManager -> entityManager.find(Post.class, post.getId()));
        managedPost.removeComment(managedPost.getComments().remove(0));
        Post updatedPost = postDao.updatePost(managedPost);
        assertThat(updatedPost.getComments().size(), equalTo(1));
    }
}
