package jpql;

import javax.persistence.*;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);

            TypedQuery<Member> findMember = em.createQuery("select m from Member m", Member.class);
            TypedQuery<String> findUsername = em.createQuery("select m.username from Member m", String.class);

            Query findUsername2 = em.createQuery("select m.username from Member m");

            //List<Member> members = findMember.getResultList();
            //String name = findUsername.getSingleResult();
            //이후 나온 Spring Data JPA ->(개선) null 또는 Optional 반환해줌

            TypedQuery<Member> query = em.createQuery("select m from Member m where m.username = :username", Member.class);
            query.setParameter("username", "member1");
            Member singleResult = query.getSingleResult();

            tx.commit();
        } catch(Exception ex) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
