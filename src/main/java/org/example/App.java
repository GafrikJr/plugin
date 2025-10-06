package org.example;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.treewalk.TreeWalk;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) throws GitAPIException, IOException {
        Git git = Git.open(new File("C:\\Users\\timur\\IdeaProjects\\GitTest\\.git"));
        List<String> files = showBranchDiff(git, "master");
        files.stream().filter(f -> f.contains("//TODO")).forEach(System.out::println);

    }

    public static List<String> showBranchDiff(Git git, String branch1) throws IOException {
        List<String> files = new ArrayList<>();
        Repository repository = git.getRepository();

        RevCommit commit = repository.parseCommit(repository.resolve("HEAD"));
        RevTree tree = commit.getTree();

        try (TreeWalk treeWalk = TreeWalk.forPath(repository, "src/main/java/org/example", tree)) {
            if (treeWalk != null && treeWalk.isSubtree()) {
                treeWalk.enterSubtree();
                while (treeWalk.next()) {
                    if (treeWalk.isSubtree()) System.out.println("It is subtree " + treeWalk.getPathString());
                    else {
                        System.out.println("File: " + treeWalk.getPathString());
                        ObjectId blobId = treeWalk.getObjectId(0);
                        ObjectLoader loader = repository.open(blobId);
                        files.add(new String(loader.getBytes()));
                    }
                }
            }

        }
        return files;
    }
}
