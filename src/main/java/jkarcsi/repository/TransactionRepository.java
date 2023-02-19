package jkarcsi.repository;

import java.util.List;

import jkarcsi.dto.gallery.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    boolean existsByArtwork(Integer artwork);
    List<Transaction> findAllByUser(String user);
}
