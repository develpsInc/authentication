package LIVTech.authentication.authentication.models;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Data
@Transactional
@Table (name = "userMemory")
public class Memories_ {
    @Id
    private Long id;
    @Lob
    @Column(name = "images",columnDefinition = "BLOB")
    private byte[] images;

    private String title;
    private String description;
    @OneToOne(
            mappedBy = "user",
            cascade = CascadeType.ALL
    )
    private User user;

}
