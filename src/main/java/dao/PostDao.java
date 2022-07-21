package dao;

import model.Post;

public interface PostDao {
    /**
     * Receives a new instance of {@link Post} and saves it
     */
    void savePost(Post post);

    /**
     * Receives a new instance of {@link Post} and updates it
     */
    Post updatePost(Post post);

    /**
     * Receives instance of {@link Post} and deletes it
     */
    void delete(long postId);

}
