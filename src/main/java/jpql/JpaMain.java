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

            em.flush();
            em.clear();

            List<Member> result = em.createQuery("select m from Member m", Member.class)
                            .getResultList();

            Member findMember = result.get(0);
            findMember.setAge(20); // update 문 나감

            // Query 타입으로 조회
            List resultList = em.createQuery("select m.username, m.age from Member m").getResultList();

            Object o = resultList.get(0);
            Object[] resultObjects = (Object[])o;
            System.out.println("username =" + resultObjects[0]);
            System.out.println("age =" + resultObjects[1]);

            // Object[] 타입으로 조회
            List<Object[]> resultList2 = em.createQuery("select m.username, m.age from Member m").getResultList();

            Object[] resultObjects2 = resultList2.get(0);
            System.out.println("username =" + resultObjects2[0]);
            System.out.println("age =" + resultObjects2[1]);

            // new 명령어로 조회
            List<MemberDTO> memberDTOs = em.createQuery("select new jpql.MemberDTO(m.username, m.age) from Member m").getResultList();
            MemberDTO memberDTO = memberDTOs.get(0);
            System.out.println("username =" + memberDTO.getUsername());
            System.out.println("age =" + memberDTO.getAge());

            tx.commit();
        } catch(Exception ex) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
