1.
run rapid/tools/copy_repository.bat
2.
run rapid/install.bat,eclipse.bat

3.
mvn archetype:generate -B -DarchetypeGroupId=org.rapid.archetypes  -DarchetypeArtifactId=rapid-flex-hibernate -DarchetypeVersion=1.0 -DgroupId=com.fly   -DartifactId=flex4demo  -DarchetypeCatalog=local
mvn archetype:generate -B -DarchetypeGroupId=org.rapid.archetypes  -DarchetypeArtifactId=rapid-springmvc-hibernate -DarchetypeVersion=1.0 -DgroupId=com.fly   -DartifactId=shdemo -Dversion=1.0 -DarchetypeCatalog=local

4.
rapid/tools/h2/start_h2_db.bat

5.
cd rapidflex
eclipse.bat
mvn package
mvn rapid:gen -Dtable=user_info

6.
导入工程