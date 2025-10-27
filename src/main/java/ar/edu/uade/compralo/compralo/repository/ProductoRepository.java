package ar.edu.uade.compralo.compralo.repository;

import ar.edu.uade.compralo.compralo.model.entity.Producto;
import jakarta.transaction.Transactional;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

public interface ProductoRepository extends Neo4jRepository<Producto, Long> {

}
