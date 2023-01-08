package study.datajpa.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@ToString(of = {"id", "name"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter @Setter
@Entity
public class Team {
    @Id @GeneratedValue
    @Column(name = "team_id")
    private Long id;
    private String name;

    @OneToMany(mappedBy = "team")
    private List<Member> members;

    @Builder
    public Team(String name) {
        this.name = name;
    }
}
