package com.gsmarin.springsecurity.controller;

import com.gsmarin.springsecurity.Entity.FeedsResponse;
import com.gsmarin.springsecurity.Entity.Tweet;
import com.gsmarin.springsecurity.Entity.TweetRequest;
import com.gsmarin.springsecurity.service.TweetService;
import com.gsmarin.springsecurity.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@AllArgsConstructor
public class TweetController {

    private final TweetService tweetService;
    private final UserService userService;

    @PostMapping("/tweets")
    public ResponseEntity<Object> createTweet(
            @RequestBody TweetRequest tweetRequest,
            JwtAuthenticationToken token) {
        var user = userService.findById(UUID.fromString(token.getName()));
        var tweet = new Tweet();

        tweet.setUser(user.get());
        tweet.setText(tweetRequest.content());

        tweetService.save(tweet);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/feeds")
    public ResponseEntity<FeedsResponse> getTweets(@RequestParam(value = "page", defaultValue = "0") int page,
                                                   @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        return tweetService.getFeeds(page, pageSize);
    }

    @DeleteMapping("/tweets/{id}")
    public ResponseEntity<Void> deleteTweet(@PathVariable("id") Long id,
                                            JwtAuthenticationToken token) {
        tweetService.deleteTweet(id, token);
        return ResponseEntity.ok().build();
    }
}
