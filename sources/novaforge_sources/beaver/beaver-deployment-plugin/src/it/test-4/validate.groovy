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

def deployment = new File(basedir, "/test/install/deployment.xml")
assert deployment.exists()

def data1 = new File(file.getPath()+"/data_1")
assert data1.exists()

def home2 = new File(file.getPath()+"/Component2")
assert home2.exists()
def data2 = new File(file.getPath()+"/Component2/data_2")
assert data2.exists()

def home4 = new File(file.getPath()+"/Component4")
assert home4.exists()
def data4 = new File(file.getPath()+"/Component4/data_4")
assert data4.exists()
def data4_11 = new File(file.getPath()+"/Component4/data_4_1.1")
assert data4_11.exists()

def home3 = new File(home4.getPath()+"/Component3")
assert home3.exists()
def data3 = new File(home3.getPath()+"/data_3")
assert data3.exists()

def _tempDir = new File(file.getPath()+"/novaforgeTemp")
assert !_tempDir.exists()

build.delete()