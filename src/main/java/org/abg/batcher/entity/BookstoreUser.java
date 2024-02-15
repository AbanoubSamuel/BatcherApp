package org.abg.batcher.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

import java.time.LocalDate;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Getter
@Setter
@ToString
@Table(name = "bookstore_user")
public class BookstoreUser {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_user_generator")
    @GenericGenerator(
            name = "sequence_user_generator",
            type = SequenceStyleGenerator.class,
            parameters = {
                    @Parameter(name = "sequence_name", value = "public.bookstore_user_id_seq"),
                    @Parameter(name = "initial_value", value = "1000"),
                    @Parameter(name = "increment_size", value = "1")
            })
    private Long id;

    @Column(nullable = false)
    private String login;

    @Column(nullable = false)
    private String password;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false)
    private long age;

    public BookstoreUser(Long id, String s, LocalDate parse, int i) {
    }
}
