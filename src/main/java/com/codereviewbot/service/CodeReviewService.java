package com.codereviewbot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codereviewbot.model.PRReview;
import com.codereviewbot.repository.PRReviewRepository;
import java.time.LocalDateTime;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CodeReviewService {

    private PRReviewRepository prReviewRepository;

    public CodeReviewService(PRReviewRepository prReviewRepository) {
        this.prReviewRepository = prReviewRepository;
    }

    public List<String> analyzeDiff(String diffJson, String repoName, int prNumber) {

        List<String> issues = new ArrayList<>();

        int issuesSize = 0;
        boolean isEmpty = false;

        if (diffJson == null || diffJson.isEmpty()) {
            issues.add("No code changes detected.");
            isEmpty = true;
            return issues;
        }
        
        // Best code practices
        if (diffJson.contains("System.out.println")) {
            issues.add("Avoid using System.out.println in production code.");
        }
        
        if (diffJson.contains("console.log")) {
            issues.add("console.log detected — remove debug logging before merging.");
        }
        
        if (diffJson.contains("TODO")) {
            issues.add("TODO found in code. Make sure it is addressed before merging.");
        }
        
        if (diffJson.contains("public void") && diffJson.split("\n").length > 200) {
            issues.add("Large method detected. Consider breaking it into smaller functions.");
        }
        
        if (diffJson.contains("// System.out") || diffJson.contains("//console.log")) {
            issues.add("Commented-out debug code detected. Remove unused commented code.");
        }
        
        if (diffJson.contains("catch (Exception") && diffJson.contains("catch (Exception e) {}")) {
            issues.add("Empty catch block detected. Avoid silently swallowing exceptions.");
        }
        
        // Security checks
        if (diffJson.contains("password=") || diffJson.contains("password =") ||
        diffJson.contains("apiKey") || 
        diffJson.contains("secretKey") || diffJson.contains("accessToken") || 
        diffJson.contains("token")) {
            issues.add("Potential sensitive information detected. Ensure no secrets are hardcoded.");
        }
        
        if (diffJson.contains("eval(")) {
            issues.add("Use of eval() detected. This can lead to security vulnerabilities.");
        }
        
        if (diffJson.contains("SELECT") && diffJson.contains("+")) {
            issues.add("Possible SQL query concatenation detected. Use prepared statements to avoid SQL injection.");
        }
        
        if (issues.isEmpty()) {
            issues.add("No obvious issues detected by the bot.");
            isEmpty = true;
        }
        
        if isEmpty {
            issuesSize = 0;
        } else {
            issuesSize = issues.size();
        }

        PRReview review = new PRReview(
            repoName,
            prNumber,
            issuesSize,
            String.join(", ", issues),
            LocalDateTime.now()
        );

        prReviewRepository.save(review);

        return issues;
    }
}