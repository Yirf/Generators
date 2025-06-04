package me.yirf.generators.plugin;

import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;

public class GeneratorsLoader implements PluginLoader {
    @Override
    public void classloader(PluginClasspathBuilder classpathBuilder) {
        MavenLibraryResolver resolver = new MavenLibraryResolver();
        resolver.addRepository(new RemoteRepository.Builder("sonatype", "default", "https://repo1.maven.org/maven2/").build());
        resolver.addDependency(new Dependency(new DefaultArtifact("org.incendo:cloud-paper:2.0.0-beta.10"), null));

        classpathBuilder.addLibrary(resolver);
    }
}
