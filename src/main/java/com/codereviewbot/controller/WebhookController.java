package com.codereviewbot.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.codereviewbot.model.PullRequestEvent;
import com.codereviewbot.service.GitHubService;
import com.codereviewbot.service.CodeReviewService;

@RestController
public class WebhookController {

    private final GitHubService gitHubService;
    private final CodeReviewService codeReviewService;

    public WebhookController(GitHubService gitHubService, CodeReviewService codeReviewService) {
        this.gitHubService = gitHubService;
        this.codeReviewService = codeReviewService;
    }

    @PostMapping("/webhook")
    public String receiveWebhook(
        @RequestHeader("X-GitHub-Event") String event,
        @RequestBody PullRequestEvent payload) {

        if (!event.equals("pull_request")) {
            return "Ignored event";
        }

        String action = payload.getAction();

        if (!action.equals("opened") && !action.equals("synchronize")) {
            return "Ignored PR action";
        }

        String owner = payload.getRepository().getOwner().getLogin();
        int prNumber = payload.getPull_request().getNumber();
        String repoName = payload.getRepository().getName();

        System.out.println("PR needs review!");
        System.out.println("Repo: " + repoName);
        System.out.println("PR #: " + prNumber);

        String files = gitHubService.getPullRequestFiles(owner, repoName, prNumber);

        System.out.println(files);

        List<String> issues = codeReviewService.analyzeDiff(files);

        for (String issue : issues) {
            System.out.println("Issue found: " + issue);
        }

        StringBuilder comment = new StringBuilder("🤖 Code Review Bot\n\n");

        if (issues.isEmpty()) {
            comment.append("✅ No issues detected. Code looks good!");
        } else {
            comment.append("⚠️ Potential issues found:\n\n");

            for (String issue : issues) {
                comment.append("- ").append(issue).append("\n");
            }
        }

        gitHubService.postPullRequestComment(owner, repoName, prNumber, comment.toString());
        
        return "PR event processed";
    }
}