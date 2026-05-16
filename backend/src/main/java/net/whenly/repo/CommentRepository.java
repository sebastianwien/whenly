package net.whenly.repo;

import java.util.List;
import java.util.UUID;
import net.whenly.domain.Comment;
import net.whenly.domain.Poll;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
  List<Comment> findByPollOrderByCreatedAtAsc(Poll poll);
}
