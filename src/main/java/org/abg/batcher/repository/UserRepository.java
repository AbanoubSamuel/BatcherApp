package org.abg.batcher.repository;

import org.abg.batcher.entity.BookstoreUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<BookstoreUser, Long> {
}
