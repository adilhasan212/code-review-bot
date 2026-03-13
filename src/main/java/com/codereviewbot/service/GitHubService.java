package com.codereviewbot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.HashMap;
import java.util.Map;

@Service
public class GitHubService {

    @Value("${github.token}")
    private String githubToken;

    @Value("${github.api.url}")
    private String githubApiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public String getPullRequestFiles(String owner, String repo, int prNumber) {

        try {
            String url = githubApiUrl + "/repos/" + owner + "/" + repo + "/pulls/" + prNumber + "/files";

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + githubToken);
            headers.set("Accept", "application/vnd.github+json");

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            return response.getBody();

        } catch (Exception e) {
            System.out.println("GitHub API call failed:");
            e.printStackTrace();
            return "Error calling GitHub API";
        }
    }

    public void postPullRequestComment(String owner, String repo, int prNumber, String comment) {
        try {
            String url = githubApiUrl + "/repos/" + owner + "/" + repo + "/issues/" + prNumber + "/comments";

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + githubToken);
            headers.set("Accept", "application/vnd.github+json");
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> body = new HashMap<>();
            body.put("body", comment);

            HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            System.out.println("Comment posted to PR!");
            System.out.println(response.getBody());

        } catch (Exception e) {
            System.out.println("Failed to post PR comment");
            e.printStackTrace();
        }
    }
}