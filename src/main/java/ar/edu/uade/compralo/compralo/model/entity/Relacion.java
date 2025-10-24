package ar.edu.uade.compralo.compralo.model.entity;

import lombok.*;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@RelationshipProperties
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Relacion {
    @Id
    @GeneratedValue
    private Long id;
    @TargetNode
    private Producto producto;
    private Double peso;
}
