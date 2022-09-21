package com.askchaitanya.urlcorta.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class DBUrl {

    @JsonProperty(value = "id")
    private UUID id;

    @JsonProperty(value = "original_url")
    private String originalUrl;

    @JsonProperty(value = "alias")
    private String alias;

    @JsonProperty(value = "created_at")
    private Date createdAt;

    @JsonProperty(value = "expiration_date")
    private Date expirationDate;

}
