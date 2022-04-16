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
            Team teamA = new Team();
            teamA.setName("teamA");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("teamB");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("member1");
            member1.setAge(10);
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("member2");
            member2.setAge(10);
            member2.setTeam(teamB);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("member3");
            member3.setAge(10);
            member3.setTeam(teamB);
            em.persist(member3);

            Member member4 = new Member();
            member4.setUsername("member4");
            member4.setAge(10);
            em.persist(member4); // member4는 team에 값이 없어 join 된게 없으니 select x

            String jpql = "select m from Member m join fetch m.team";
            List<Member> members = em.createQuery(jpql, Member.class)
                            .getResultList();

            for(Member m : members) {
                System.out.println(m.toString());
            }

            String jpql2 = "select t from Team t join fetch t.members where t.name = 'teamA' ";
            List<Team> teams = em.createQuery(jpql2, Team.class).getResultList();

            for(Team team : teams) {
                System.out.println("teamname = " + team.getName() + ", team = " + team);
                for(Member member : team.getMembers()) {
                    System.out.println("->username =" + member.getUsername() + ", member =" + member);
                }
            }

            tx.commit();
        } catch(Exception ex) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
