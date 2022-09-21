package com.askchaitanya.urlcorta.services;

import com.askchaitanya.urlcorta.dto.RequestDTO;
import com.askchaitanya.urlcorta.dto.ResponseDTO;
import com.askchaitanya.urlcorta.models.DBUrl;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Service
public class UrlShortenerService {

    @Value("${corta.default.short_url_length}")
    private int defaultShortUrlLength;

    @Value("${corta.default.expiration_time_in_days}")
    private int defaultExpirationTimeInDays;

    @Autowired
    DBUrlService dbUrlService;

    public Optional<ResponseDTO> shortenUrl(RequestDTO requestDTO) {
        String shortUrl = isShortUrlAlreadyPresent(requestDTO.getCustomAlias())
                ? generateNewShortUrl(defaultShortUrlLength)
                : requestDTO.getCustomAlias();
        Date expirationDate = getExpirationDate(requestDTO.getExpirationTimeInDays());
        DBUrl dbUrlPayload = DBUrl.builder()
                .originalUrl(requestDTO.getOriginalUrl())
                .alias(shortUrl)
                .expirationDate(expirationDate)
                .build();
        int insertedRows = dbUrlService.insertDBUrl(dbUrlPayload.getOriginalUrl(), dbUrlPayload.getAlias(), dbUrlPayload.getExpirationDate());
        Optional<DBUrl> insertedDBUrlOptional = Optional.empty();
        if (insertedRows > 0) {
            insertedDBUrlOptional = dbUrlService.getDBUrl(dbUrlPayload.getAlias());
        }
        DBUrl insertedDBUrl;
        if (insertedDBUrlOptional.isPresent()) {
            insertedDBUrl = insertedDBUrlOptional.get();
        } else {
            return Optional.empty();
        }
        //catch error and populate message in response dto
        ResponseDTO responseDTO = ResponseDTO.builder()
                .id(insertedDBUrl.getId())
                .originalUrl(insertedDBUrl.getOriginalUrl())
                .alias(insertedDBUrl.getAlias())
                .message("Short url generated successfully")
                .createdAt(insertedDBUrl.getCreatedAt())
                .expirationDate(insertedDBUrl.getExpirationDate())
                .build();
        return Optional.of(responseDTO);
    }

    private String generateNewShortUrl(int n) {
        String randomAlphaNumericString = RandomStringUtils.randomAlphanumeric(n);
        while (isShortUrlAlreadyPresent(randomAlphaNumericString)) {
            randomAlphaNumericString = RandomStringUtils.randomAlphanumeric(n);
        }
        return randomAlphaNumericString;
    }

    // generate random string using Math.random() and character pool
    private String generateAlphaNumericString(int n) {
        // chose a Character random from this String
        String AlphaNumericCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            // generate a random number between 0 to AlphaNumericString variable length
            int index = (int) (AlphaNumericCharacters.length() * Math.random());
            sb.append(AlphaNumericCharacters.charAt(index));
        }
        return sb.toString();
    }

    // returns default expiration time in case of invalid argument
    private Date getExpirationDate(Integer expirationTimeInDays) {
        Date date = new Date();
        if (Objects.isNull(expirationTimeInDays) || expirationTimeInDays < 0) {
            expirationTimeInDays = defaultExpirationTimeInDays;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, expirationTimeInDays);
        return cal.getTime();
    }

    public Optional<String> getOriginalUrl(String shortUrl) {
        return dbUrlService.getDBUrl(shortUrl).map(DBUrl::getOriginalUrl);
    }

    private boolean isShortUrlAlreadyPresent(String shortUrl) {
        return !StringUtils.hasLength(shortUrl) || dbUrlService.getDBUrl(shortUrl).isPresent();
    }

}
