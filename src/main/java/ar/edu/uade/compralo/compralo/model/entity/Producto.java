package ar.edu.uade.compralo.compralo.model.entity;

import lombok.*;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.Set;

@Node
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Producto {
    @Id
    @GeneratedValue
    private Long id;
    private String nombre;
    private Double precio;
    @Relationship(type = "RELACIONADO")
    private Set<Relacion> relacionados;
}
