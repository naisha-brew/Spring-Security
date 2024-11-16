package com.gsmarin.springsecurity.service;
import com.gsmarin.springsecurity.Entity.FeedItem;
import com.gsmarin.springsecurity.Entity.FeedsResponse;
import com.gsmarin.springsecurity.Entity.Tweet;
import com.gsmarin.springsecurity.Repository.TweetRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TweetService {
    private final TweetRepository tweetRepository;

    public void save(Tweet tweet) {
        try {
            tweetRepository.save(tweet);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void deleteTweet(Long id, JwtAuthenticationToken token) {

        var tweet = tweetRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if(tweet.getUser().getId().equals(UUID.fromString(token.getName()))
        || getPrincipalInfo(token).equals("Admin")) {
            tweetRepository.delete(tweet);
        }
        else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    private String getPrincipalInfo(JwtAuthenticationToken token) {
        Collection<String> authorities = token.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        Map<String, Object> info = new HashMap<>();
        info.put("authorities", authorities);
        if(info.get("authorities").toString().contains("SCOPE_Admin")) {
            return "Admin";
        }
        return "Basic";
    }

    public ResponseEntity<FeedsResponse> getFeeds(int page, int pagesize) {
        var tweets = tweetRepository.findAll(PageRequest.of(page, pagesize,
                Sort.Direction.DESC, "creationDateTime"))
                .map(tweet -> new FeedItem(
                        tweet.getTweetId(), tweet.getText(),
                        tweet.getUser().getUsername()
                ));
        return ResponseEntity.ok(new FeedsResponse(tweets.getContent(),
                page, pagesize, tweets.getTotalPages(),
                tweets.getTotalElements()));
    }

}
