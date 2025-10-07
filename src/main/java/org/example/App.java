package org.example;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.patch.FileHeader;
import java.io.ByteArrayOutputStream;
import java.util.List;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class App {
    public static void main( String[] args ) throws Exception {
        Git git = Git.open(new File("C:\\Users\\timur\\IdeaProjects\\dependencies-counter-maven-plugin\\.git"));
//        List<String> files = getFilesContent(git, "src/main/java/org/example", "develop");
//        files.stream().filter(f -> f.contains("//TODO")).forEach(System.out::println);
        List<String> diffs = showDetailedBranchDiff(git, "master", "develop");
        diffs.stream().filter(d -> d.contains("//TODO")).forEach(System.out::println);

    }

    public static List<String> getFilesContent(Git git, String dirPath, String branchName) throws IOException {
        List<String> files = new ArrayList<>();
        Repository repository = git.getRepository();

        RevCommit commit = repository.parseCommit(repository.resolve("refs/heads/%s".formatted(branchName)));
        RevTree tree = commit.getTree();

        try (TreeWalk treeWalk = TreeWalk.forPath(repository, dirPath, tree)) {
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

        //TODO-AGONA124 fd
    }

    public static List<String> showDetailedBranchDiff(Git git, String branch1, String branch2) throws Exception {
        List<String> differs = new ArrayList<>();

        Repository repository = git.getRepository();

        RevCommit commit1 = repository.parseCommit(repository.resolve(branch1));
        RevCommit commit2 = repository.parseCommit(repository.resolve(branch2));

        RevTree tree1 = commit1.getTree();
        RevTree tree2 = commit2.getTree();

        // Используем DiffFormatter для детального вывода
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             DiffFormatter formatter = new DiffFormatter(out)) {

            formatter.setRepository(repository);
            formatter.setContext(3); // Количество строк контекста

            List<DiffEntry> diffs = formatter.scan(tree1, tree2);

            System.out.println("Детальные различия между " + branch1 + " и " + branch2 + ":");
            System.out.println("=========================================");

            for (DiffEntry entry : diffs) {
                System.out.println("\n--- Файл: " + entry.getOldPath() +
                        " -> " + entry.getNewPath());
                System.out.println("Тип изменения: " + entry.getChangeType());

                // Форматируем и выводим diff
                FileHeader header = formatter.toFileHeader(entry);
                formatter.format(header);

                String diffText = out.toString();
                if (!diffText.trim().isEmpty()) {
                    differs.add(diffText);
                }
                out.reset();
            }
            return differs;
        }
    }
}
