https://github.com/SouljaBoy-tell-em/NetCracker/assets/60592559/342d24a8-ee26-4fae-b8dc-b86405245760


# URLDownloader

This program allows you to parse a web page or a file by its link.

## English documentation:

### BUILDING:
The project is built on the Maven archetype (pom.xml). The Jsoup dependency is used for convenient file parsing. Dependencies such as junit, batik-css, commons-validator, joda-time and commons-io are also used.

```sh
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>3.8.1</version>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>org.jsoup</groupId>
      <artifactId>jsoup</artifactId>
      <version>1.16.1</version>
    </dependency>
    <dependency>
      <groupId>org.apache.xmlgraphics</groupId>
      <artifactId>batik-css</artifactId>
      <version>1.17</version>
    </dependency>
    <dependency>
      <groupId>commons-validator</groupId>
      <artifactId>commons-validator</artifactId>
      <version>1.7</version>
    </dependency>
    <dependency>
      <groupId>joda-time</groupId>
      <artifactId>joda-time</artifactId>
      <version>2.2</version>
    </dependency>
    <dependency>
      <groupId>commons-io</groupId>
      <artifactId>commons-io</artifactId>
      <version>2.14.0</version>
    </dependency>
```

### Parsing a web page by link:
Parsing is done while preserving the folder structure of the files located at the specified attributes in the link. HTML, CSS, and JS files are parsed.

### Parsing a file by link:
The file is simply downloaded.

### Compile from the console: 
The following plugin is used for compiling and passing arguments from the console:

```sh
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-shade-plugin</artifactId>
    <version>2.1</version>
        <executions>
            <execution>
              <phase>package</phase>
              <goals>
                <goal>shade</goal>
              </goals>
              <configuration>
                <transformers>
                  <transformer
                          implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                    <mainClass>org.example.Main</mainClass>
                  </transformer>
                </transformers>
              </configuration>
            </execution>
        </executions>
</plugin>
```

### How to compile?
To start, open the terminal in a project folder. To compile a Maven project, follow these steps:
```sh
$ mvn install
```
```sh
$ mvn package
```
```sh
$ mvn compile
```

After that, a folder named "target" will be created, which will contain the next jar file:
```sh
URLDownloader-1.0-SNAPSHOT.jar 
```
Next, we need to navigate to the "target" folder:
```sh
$ cd target
```
And run the jar file:
```sh
$ java -jar URLDownloader-1.0-SNAPSHOT.jar <your_link> <your_path>
```
The project will start compiling and the web page or file will be parsed into the specified directory(<your_path>).

## Русская документация:

### СБОРКА:
Проект построен на архетипе Maven(pom.xml). Для удобства парсинга файлов использована зависимость Jsoup. 
Также использованы зависимости: junit, batik-css, commons-validator, joda-time и commons-io.

```sh
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>3.8.1</version>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>org.jsoup</groupId>
      <artifactId>jsoup</artifactId>
      <version>1.16.1</version>
    </dependency>
    <dependency>
      <groupId>org.apache.xmlgraphics</groupId>
      <artifactId>batik-css</artifactId>
      <version>1.17</version>
    </dependency>
    <dependency>
      <groupId>commons-validator</groupId>
      <artifactId>commons-validator</artifactId>
      <version>1.7</version>
    </dependency>
    <dependency>
      <groupId>joda-time</groupId>
      <artifactId>joda-time</artifactId>
      <version>2.2</version>
    </dependency>
    <dependency>
      <groupId>commons-io</groupId>
      <artifactId>commons-io</artifactId>
      <version>2.14.0</version>
    </dependency>
```

### Парсинг веб-страницы по ссылке:
Парсинг происходит в соответствии с сохранением папочной структуры файлов, расположенных по ссылке в определенных аттрибутах. Парсятся HTML, CSS и JS файлы. 

### Парсинг файла по ссылке:
Просто скачивается файл.

### Компиляция из консоли: 
Для компиляции и подачи аргументов из консоли подключен следующий плагин:

```sh
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-shade-plugin</artifactId>
    <version>2.1</version>
        <executions>
            <execution>
              <phase>package</phase>
              <goals>
                <goal>shade</goal>
              </goals>
              <configuration>
                <transformers>
                  <transformer
                          implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                    <mainClass>org.example.Main</mainClass>
                  </transformer>
                </transformers>
              </configuration>
            </execution>
        </executions>
</plugin>
```

### Как скомпилировать?
Для начала откройте консоль/терминал в папке с проектом. Чтобы скомпилировать Maven проект, следуйте этим шагам:
:
```sh
$ mvn install
```
```sh
$ mvn package
```
```sh
$ mvn compile
```

После этого будет создана папка target, в которой будет находится следующий jar файл:
```sh
URLDownloader-1.0-SNAPSHOT.jar 
```
Далее, мы должны перейти в папку "target":
```sh
$ cd target
```
И запустить jar файл:
```sh
$ java -jar URLDownloader-1.0-SNAPSHOT.jar <your_link> <your_path>
```
Проект начнет компилироваться и веб страница или файл будут спаршены в указанную директорию(<your_path>).
