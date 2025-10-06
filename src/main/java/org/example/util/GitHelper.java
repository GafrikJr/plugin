package org.example.util;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;

public class GitHelper {

    public static String getCurrentBranch() throws IOException {
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        try (Repository repository = builder
                .readEnvironment()
                .findGitDir(new File("."))
                .build()) {

            return repository.getBranch();
        }
    }
}
