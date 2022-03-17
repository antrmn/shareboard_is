package common;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.flywaydb.core.api.output.MigrateResult;

import javax.annotation.Resource;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.sql.DataSource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@TransactionManagement(TransactionManagementType.BEAN)
class FlywayMigrator {

    private Path uploadRoot = Path.of(System.getProperty("openejb.home"), "uploads");

    @Resource(name = "jdbc/shareboardDB")
    private DataSource dataSource;

    public void postConstruct(@Observes @Initialized(ApplicationScoped.class) Object o) throws IOException {
        if (dataSource == null) {
            throw new RuntimeException("Cannot migrate, no datasource configured!");
        }
        FluentConfiguration config = Flyway.configure()
                .dataSource(dataSource)
                .validateOnMigrate(false)
                .locations("classpath:db/migration");
        Flyway flyway = new Flyway(config);
        MigrateResult migrate = flyway.migrate();
        if(!migrate.migrations.isEmpty()){
            Files.createDirectories(uploadRoot);

            InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("sample/pictures.zip");
            if(resourceAsStream == null){
                throw new RuntimeException("pictures.zip not found");
            }

            try(ZipInputStream zipStream = new ZipInputStream(resourceAsStream)){
                ZipEntry entry = zipStream.getNextEntry();
                byte[] buffer = new byte[1024];
                while (entry != null) {
                    File newFile = new File(uploadRoot.toFile(), entry.getName());
                    // fix for Windows-created archives
                    File parent = newFile.getParentFile();
                    if (!parent.isDirectory() && !parent.mkdirs()) {
                        throw new IOException("Failed to create directory " + parent);
                    }

                    // write file content
                    try(FileOutputStream fos = new FileOutputStream(newFile)){
                        int len;
                        while ((len = zipStream.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                        entry = zipStream.getNextEntry();
                    }
                }
            }

        }
    }
}
