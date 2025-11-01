package com.example.pos_backend.repository;
import com.example.pos_backend.model.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TableRepository extends JpaRepository<Table, Long>{
    // YENİ EKLENEN METOT: (DatabaseInitializerService'in ihtiyacı olan)
    Optional<Table> findByTableName(String tableName);
}
