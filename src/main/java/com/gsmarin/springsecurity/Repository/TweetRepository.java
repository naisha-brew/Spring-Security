package com.gsmarin.springsecurity.Repository;

import com.gsmarin.springsecurity.Entity.Tweet;
import org.hibernate.query.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TweetRepository extends JpaRepository<Tweet, Long> {
}
