package study.datajpa.entity;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.repository.MemberRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static java.lang.Thread.*;

@Transactional
@SpringBootTest
@Rollback(value = false)
class MemberTest {
    @PersistenceContext
    EntityManager em;

    @Autowired
    MemberRepository memberRepository;

    @Test
    void testEntity() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        em.persist(teamA);
        em.persist(teamB);

        Member member1 = Member.builder()
                .username("member1")
                .age(23)
                .team(teamA)
                .build();
        Member member2 = Member.builder()
                .username("member2")
                .age(25)
                .team(teamA)
                .build();
        Member member3 = Member.builder()
                .username("member1")
                .age(23)
                .team(teamB)
                .build();
        Member member4 = Member.builder()
                .username("member1")
                .age(23)
                .team(teamB)
                .build();

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        // 강제 SQL 쓰기, 캐시 비움
        em.flush();
        em.clear();

        List<Member> members = em.createQuery("select m from Member m", Member.class).getResultList();
        for (Member member : members) {
            System.out.println("member = " + member);
            System.out.println("member.team = " + member.getTeam());
        }
    }

    @Test
    void JpaEventBaseEntity() throws InterruptedException {
        // given
        Member member = new Member("member1", 20);
        memberRepository.save(member);

        sleep(100);
        member.setUsername("member2");

        em.flush();
        em.clear();

        //when
        Member findMember = memberRepository.findById(member.getId()).orElseThrow();

        //then
        System.out.println("findMember = " + findMember.getCreatedDate());
        System.out.println("findMember = " + findMember.getLastModifiedDate());
        System.out.println("findMember.CREATED_BY = " + findMember.getCreatedBy());
        System.out.println("findMember.MODIFIED_BY = " + findMember.getLastModifiedBy());
    }
}