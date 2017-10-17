def env = System.getenv()
String mvnBin = env['M2_HOME']+ "/bin/" + mvnBin
AntBuilder ant = new AntBuilder()

//clean the previous integration-test
ant.delete(dir:localRepositoryPath.getPath()+"/org/novaforge/beaver/test")
ant.delete(dir:basedir.getPath()+"/test")


println "*************************************************************"
println "***** Starting : installing DATA to local repository"
println "-------------------------------------------------------------"

//Process to install in the local repository each data artifact
scanner = ant.fileScanner {
    fileset(dir:basedir.getPath()+"/ressources/Data") {
        include(name:"**/*.zip")
    }
}
for (zipFile in scanner) {
   
   assert zipFile instanceof File
   assert zipFile.name.endsWith(".zip")
   println "***** ZIP : "+zipFile.path
   
   File pomFile = new File(zipFile.parent+"/pom.xml")
   assert pomFile.exists()
   println "***** POM : "+pomFile.path
   def process = [mvnBin, "install:install-file", "-Dfile="+zipFile.getPath(),"-DpomFile="+pomFile,"-DlocalRepositoryPath="+localRepositoryPath].execute()
   process.waitFor()
   process.in.eachLine { line -> if (line.contains("BUILD")) assert line =~ /SUCCESS/ }
   println "-------------------------------------------------------------"
}

//Process to install in the local repository each data artifact
scanner = ant.fileScanner {
   fileset(dir:basedir.getPath()+"/ressources/Data") { 
      include(name:"**/*.tar.gz") 
   }
}
for (tarFile in scanner) {
   
   assert tarFile instanceof File
   assert tarFile.name.endsWith(".tar.gz")   
   println "***** TAR.GZ : "+tarFile.path
   File pomFile = new File(tarFile.parent+"/pom.xml")
   assert pomFile.exists()
   println "***** POM : "+pomFile.path
   def process = [mvnBin, "install:install-file", "-Dfile="+tarFile.path,"-DpomFile="+pomFile,"-DlocalRepositoryPath="+localRepositoryPath].execute()
   process.waitFor()
   process.in.eachLine { line -> if (line.contains("BUILD")) assert line =~ /SUCCESS/ }
   println "-------------------------------------------------------------"
}

println "***** END"
println "*************************************************************"

println "*************************************************************"
println "***** Starting : installing PROCESS/SERVER to local repository"
println "-------------------------------------------------------------"
//Process to install in the local repository each process artifact
scanner = ant.fileScanner {
   fileset(dir:basedir.getPath()+"/ressources/Process") {
       include(name:"**/pom.xml")
   }
   fileset(dir:basedir.getPath()+"/ressources/Server") {
       include(name:"**/pom.xml")
   }
}

for (pomFile in scanner) {
  assert pomFile instanceof File
  println "***** POM : "+pomFile.path
    
  def process = [mvnBin, "clean","install","-f",pomFile.path, "-Dmaven.repo.local="+localRepositoryPath].execute()
  process.waitFor()
   process.in.eachLine { line -> if (line.contains("BUILD")) assert line =~ /SUCCESS/ }
  
println "-------------------------------------------------------------"
println "***** END"
println "*************************************************************"
  
println "*************************************************************"
println "***** Starting : copy existing install "
println "-------------------------------------------------------------"

// here is an example of a block of Ant inside GroovyMarkup
ant.sequential {
    myDir = basedir.getPath() + "/test/install"
    mkdir(dir:myDir)
    copy(todir:myDir) {
        fileset(dir: basedir.getPath() + "/ressources/install") {
            include(name:"**/*")
        }
    }
}
println "-------------------------------------------------------------"
println "***** END"
println "*************************************************************"
}