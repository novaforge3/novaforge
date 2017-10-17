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

def copyTest = new File(file.getPath()+"/Component4/Component3/copy")
assert copyTest.exists()

def replaceTest = new File(file.getPath()+"/Component4/Component3/copy/replace.test")
assert replaceTest.exists()
Scanner replaceSc = new Scanner(replaceTest);
while ( replaceSc.hasNext() )
{
  String currentLine = replaceSc.nextLine();
  assert currentLine !=~ "@replace@"
}

def replaceExpTest = new File(file.getPath()+"/Component4/Component3/copy/replaceExpression.test")
assert replaceExpTest.exists()
Scanner ExpSc = new Scanner(replaceExpTest)
while ( ExpSc.hasNext() )
{
  String currentLine = ExpSc.nextLine()
  assert currentLine.equals("OK")
}

def commentTest = new File(file.getPath()+"/data_1")
assert commentTest.exists()
Scanner ComSc = new Scanner(commentTest)
while ( ComSc.hasNext() )
{
  String currentLine = ComSc.nextLine()
  assert currentLine =~ "#"
}


def _tempDir = new File(file.getPath()+"/novaforgeTemp")
assert !_tempDir.exists()


build.delete()
