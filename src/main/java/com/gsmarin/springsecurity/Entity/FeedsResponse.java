package com.gsmarin.springsecurity.Entity;

import java.util.List;

public record FeedsResponse(List<FeedItem> feedItems,
                            int page, int pageSize,
                            int totalPages, long totalCount) {
}
