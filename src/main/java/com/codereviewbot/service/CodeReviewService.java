package com.codereviewbot.service;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class CodeReviewService {

    public List<String> analyzeDiff(String diffJson) {

        List<String> issues = new ArrayList<>();

        if (diffJson == null || diffJson.isEmpty()) {
            issues.add("No code changes detected.");
            return issues;
        }

        if (diffJson.contains("System.out.println")) {
            issues.add("Avoid using System.out.println in production code.");
        }

        if (diffJson.contains("console.log")) {
            issues.add("console.log detected — remove debug logging before merging.");
        }

        if (diffJson.contains("TODO")) {
            issues.add("TODO found in code. Make sure it is addressed before merging.");
        }

        if (issues.isEmpty()) {
            issues.add("No obvious issues detected by the bot.");
        }

        return issues;
    }
}