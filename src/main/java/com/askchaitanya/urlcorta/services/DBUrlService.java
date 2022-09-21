package com.askchaitanya.urlcorta.services;

import com.askchaitanya.urlcorta.models.DBUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class DBUrlService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public int insertDBUrl(String originalUrl, String shortUrl, Date expirationDate) {
        // use NamedParameterJdbcTemplate
        String sql = "INSERT INTO blackops.urls (original_url,alias,expiration_date) values (?, ?, ?)";
        return jdbcTemplate.update(sql, originalUrl, shortUrl, expirationDate);
    }

    @Cacheable(value = "urls", key = "#shortUrl")
    public Optional<DBUrl> getDBUrl(String shortUrl) {
        String sql = "SELECT * FROM blackops.urls WHERE alias = ?";
        DBUrl dbUrl;
        try {
            dbUrl = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> DBUrl.builder()
                    .id(UUID.fromString(rs.getString(1)))
                    .originalUrl(rs.getString(2))
                    .alias(rs.getString(3))
                    .createdAt(rs.getTimestamp(4))
                    .expirationDate(rs.getTimestamp(5))
                    .build(), shortUrl);
        } catch (EmptyResultDataAccessException e) {
            dbUrl = null;
        }
        if (Objects.nonNull(dbUrl)) {
            return Optional.of(dbUrl);
        } else {
            return Optional.empty();
        }
    }

}
