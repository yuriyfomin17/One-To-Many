package util;
import model.Comment;
import org.apache.commons.lang3.RandomStringUtils;
import model.Post;

public class PostDataGenerator {

    public static Post createRandomPost(){
        Post post = new Post();
        post.setName(RandomStringUtils.randomAlphabetic(3));
        return post;
    }


    public static Comment createRandomComment(){
        Comment comment = new Comment();
        comment.setReview(RandomStringUtils.randomAlphabetic(10));
        return comment;
    }

}