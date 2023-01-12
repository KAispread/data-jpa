package study.datajpa.entity;

import lombok.*;

import javax.persistence.*;

// 연관관계 필드는 ToString 시 무한루프 가능성이 있음.
@ToString(of = {"id", "username", "age"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@NamedQuery(
        name = "Member.findByUsername",
        query="select m from Member m where m.username = :username"
)
@NamedEntityGraph(name = "Member.all", attributeNodes = @NamedAttributeNode("team"))
public class Member extends BaseEntity {
    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String username;
    @Column(nullable = false)
    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    public Member(String username, int age) {
        this.username = username;
        this.age = age;
    }

    // 연관관계 편의 메서드
    public void setTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }

    @Builder
    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;
        if (team != null) {
            this.team = team;
        }
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
