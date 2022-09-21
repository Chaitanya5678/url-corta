package com.askchaitanya.urlcorta.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ResponseDTO {

    @JsonProperty(value = "id")
    private UUID id;

    @JsonProperty(value = "original_url")
    private String originalUrl;

    @JsonProperty(value = "alias")
    private String alias;

    @JsonProperty(value = "message")
    private String message;

    @JsonProperty(value = "created_at")
    private Date createdAt;

    @JsonProperty(value = "expiration_date")
    private Date expirationDate;

}
