package com.example.pos_backend.repository;
import com.example.pos_backend.model.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface TableRepository extends JpaRepository<Table, Long>{
}
