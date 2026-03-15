package com.codereviewbot.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pr_reviews")
public class PRReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String repoName;

    private int prNumber;

    private int issuesFound;

    private String issueSummary;

    private LocalDateTime createdAt;

    public PRReview() {}

    public PRReview(String repoName, int prNumber, int issuesFound, String issueSummary, LocalDateTime createdAt) {
        this.repoName = repoName;
        this.prNumber = prNumber;
        this.issuesFound = issuesFound;
        this.issueSummary = issueSummary;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getRepoName() {
        return repoName;
    }

    public int getPrNumber() {
        return prNumber;
    }

    public int getIssuesFound() {
        return issuesFound;
    }

    public String getIssueSummary() {
        return issueSummary;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    public void setPrNumber(int prNumber) {
        this.prNumber = prNumber;
    }

    public void setIssuesFound(int issuesFound) {
        this.issuesFound = issuesFound;
    }

    public void setIssueSummary(String issueSummary) {
        this.issueSummary = issueSummary;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
