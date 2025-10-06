package org.example;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) throws GitAPIException, IOException {
        Git git = Git.open(new File("C:\\Users\\timur\\IdeaProjects\\GitTest\\.git"));
        showBranchDiff()
    }

    public static void showBranchDiff(Git git, String branch1, String branch2) throws IOException {
        Repository repository = git.getRepository();

        RevCommit commit1 = repository.parseCommit(repository.resolve(branch1));
        RevCommit commit2 = repository.parseCommit(repository.resolve(branch2));

        System.out.println(commit1.getName());
        System.out.println(commit2.getName());
    }
}
