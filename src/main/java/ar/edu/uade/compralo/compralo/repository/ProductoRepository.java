package ar.edu.uade.compralo.compralo.repository;

import ar.edu.uade.compralo.compralo.model.entity.Producto;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface ProductoRepository extends Neo4jRepository<Producto, Long> { }
