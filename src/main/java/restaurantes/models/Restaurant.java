package restaurantes.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
@Table(name="RESTAURANTS")
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    @NotBlank(message = "El nombre no puede estar en blanco")
    private String name;
    @Column
    @NotNull(message = "El nombre no puede estar en blanco")
    @Pattern(regexp="\\d{9}", message = "Debe tener 9 dígitos")
    private String number;
    @Column()
    @Builder.Default
    private boolean isDeleted=false;
    @Column
    @NotBlank(message = "La dirección no puede estar en blanco")
    private String adress;
    @Column
    private LocalDate creationD;
    @Column
    private LocalDate modificationD;

}
