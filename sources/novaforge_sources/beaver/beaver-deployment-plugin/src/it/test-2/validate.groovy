File build = new File(basedir, "/build.log")
Scanner sc = new Scanner(build);
while ( sc.hasNext() )
{
  String currentLine = sc.nextLine();
  assert currentLine !=~ "[ERROR]"
}

def file = new File(basedir, "/test/install/")
assert file.exists()

def deploymentlog = new File(file.getPath()+"/logs/deployment.log")
assert deploymentlog.exists()

def simulatelog = new File(file.getPath()+"/logs/simulation_report.log")
assert simulatelog.exists()

def _tempDir = new File(file.getPath()+"/logs/novaforgeTemp")
assert !_tempDir.exists()

build.delete()
