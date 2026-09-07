package com.example.library.repository;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.library.entity.BillGroup;
public interface BillGroupRepository extends JpaRepository<BillGroup, Long> {
    Optional<BillGroup> findByIdAndOwnerUsername(Long id, String ownerUsername);
}
