package dao;

import model.Post;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.function.Consumer;
import java.util.function.Function;

public class PostDaoImpl implements PostDao {
    private EntityManagerFactory emf;
    public PostDaoImpl(EntityManagerFactory entityManagerFactory){ this.emf = entityManagerFactory; }

    @Override
    public void savePost(Post post) {
        performWithinPersistentContext(entityManager -> entityManager.persist(post));
    }

    @Override
    public Post updatePost(Post post) {
        return performReturningWithinPersistentContext(entityManager -> entityManager.merge(post));
    }

    @Override
    public void delete(long postId) {
        performWithinPersistentContext(entityManager -> {
            Post post = entityManager.find(Post.class, postId);
            entityManager.remove(post);
        });
    }

    private void performWithinPersistentContext(Consumer<EntityManager> entityManagerConsumer){
        performReturningWithinPersistentContext( entityManager -> {
            entityManagerConsumer.accept(entityManager);
            return null;
        });
    }

    private <T> T performReturningWithinPersistentContext(Function<EntityManager, T> entityManagerTFunction){
        EntityManager entityManager = this.emf.createEntityManager();
        entityManager.getTransaction().begin();
        T result;
        try {
            result = entityManagerTFunction.apply(entityManager);
            entityManager.getTransaction().commit();
            return result;
        } catch (Exception e){
            entityManager.getTransaction().rollback();
            throw e;
        } finally {
            entityManager.close();
        }
    }
}
