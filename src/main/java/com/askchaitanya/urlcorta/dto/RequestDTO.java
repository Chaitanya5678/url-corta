package com.askchaitanya.urlcorta.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class RequestDTO {

    @JsonProperty(value = "original_url", required = true)
    private String originalUrl;

    @JsonProperty(value = "custom_alias")
    private String customAlias;

    @JsonProperty(value = "expiration_time_in_days", defaultValue = "1")
    private Integer expirationTimeInDays;

}
