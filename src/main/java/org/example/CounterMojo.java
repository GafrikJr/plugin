package org.example;

import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.example.util.GitHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Mojo(name = "dependency-counter", defaultPhase = LifecyclePhase.COMPILE)
public class CounterMojo extends AbstractMojo {
    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject project;

    @Parameter(property = "scope")
    private String scope = "test";

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        List<Dependency> dependencies = project.getDependencies();
        long dependenciesCount =
                dependencies.stream().
                filter(d -> (scope == null || scope.isEmpty() || scope.equals(d.getScope()))).count();
        getLog().info("Кол-во test зависимостей: " + dependenciesCount);
        try {
            getLog().info(GitHelper.getCurrentBranch());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
