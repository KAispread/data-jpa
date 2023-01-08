package study.datajpa.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Member {
    @Id @GeneratedValue
    private Long id;
    private String username;

    @Builder
    public Member(String username) {
        this.username = username;
    }
}
