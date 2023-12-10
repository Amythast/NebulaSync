package com.nebula.common.security.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Date;

/**
 * @author feifeixia
 * @date 2023/11/23-23:30
 */
@Slf4j
public class OAuth2RestTemplateWithScope extends RestTemplate {
	private final RestTemplate restTemplate;
	//todo config oauth2
	//@Value("${security.oauth2.client.access-token-uri}")
	private String accessTokenUrl;
	//@Value("${remote.client-id}")
	private String clientId;
	//@Value("${remote.client-secret}")
	private String clientSecret;
	//todo
	// scope is kind of client's permission, for different client should be different scopes
	private String scopes = "server";

	public OAuth2RestTemplateWithScope(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public OAuth2RestTemplateWithScope(RestTemplate restTemplate, String scopes) {
		this.restTemplate = restTemplate;
		this.scopes = scopes;
	}

	public OAuth2AccessToken getAccessToken(String accessTokenUrl, String clientId, String clientSecret, String scopes) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(accessTokenUrl)
			.queryParam("client_id", clientId);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> bodyParams = new LinkedMultiValueMap<>();
		bodyParams.add("client_secret", clientSecret);
		bodyParams.add("grant_type", "client_credentials");
		bodyParams.add("scope", scopes);
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(bodyParams, headers);
		ResponseEntity<String> responseEntity = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, requestEntity, String.class);
		if (responseEntity.getStatusCode() == HttpStatus.OK) {
			String response = responseEntity.getBody();
			ObjectMapper objectMapper = new ObjectMapper();
			ClientTokenResponse res = null;
			try {
				res = objectMapper.readValue(response, ClientTokenResponse.class);
			} catch (JsonProcessingException e) {
				log.error("Error: " + e.getMessage());
				return null;
			}
			DefaultOAuth2AccessToken accessToken = null;
			if (res != null && res.getData() != null) {
				accessToken = new DefaultOAuth2AccessToken(res.getData().getToken());
				accessToken.setExpiration(new Date(System.currentTimeMillis() + res.getData().getExpiresIn()));
			}
			return accessToken;
		} else {
			log.error("getAccessToken Error: " + responseEntity.getStatusCode());
			return null;
		}
	}

	public OAuth2AccessToken getAccessTokenForFeign() {
		return getAccessToken(accessTokenUrl, clientId, clientSecret, scopes);
	}
}
