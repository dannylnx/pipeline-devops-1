import pipeline.*

def call(String chosenStages){

	def utils  = new test.UtilMethods()

	def pipelineStages = (utils.isCIorCD().contains('ci')) ? ['buildAndTest','sonar','runJar','rest','nexusCI','createRelease'] : ['downloadNexus','runDownloadedJar','rest','nexusCD']

	def stages = utils.getValidatedStages(chosenStages, pipelineStages)

	stages.each{
		stage(it){
			try {
				"${it}"()
			}
			catch(Exception e) {
				error "Stage ${it} tiene problemas: ${e}"
			}
		}
	}
}

def buildAndTest(){
	sh './gradlew clean build'
}

def sonar(){
	def sonarhome = tool 'sonar-scanner'
    sh "${sonarhome}/bin/sonar-scanner -Dsonar.projectKey=ejemplo-gradle -Dsonar.java.binaries=build"
}

def runJar(){
	sh "nohup bash gradlew bootRun &"
	sleep 20
}

def rest(){
	sh "curl -X GET http://localhost:8081/rest/mscovid/test?msg=testing"
}

def nexusCI(){
    nexusPublisher nexusInstanceId: 'nexus', nexusRepositoryId: 'test-nexus', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: 'jar', filePath: "build/DevOpsUsach2020-0.0.1.jar"]], mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging: 'jar', version: "0.0.1-${env.GIT_BRANCH}"]]]  
}

def createRelease(){
	def git = new pipeline.git.GitMethods()

	if (git.checkIfBranchExists('release-v1-0-0')){
		if (git.isBranchUpdated(env.GIT_BRANCH, 'release-v1-0-0')){
			println 'La rama ya está creada y actualizada contra ' + env.GIT_BRANCH
		} else {
			git.deleteBranch('release-v1-0-0')
			git.createBranch('release-v1-0-0', env.GIT_BRANCH)
		}
	} else {
		git.createBranch('release-v1-0-0', env.GIT_BRANCH)
	}
}

def downloadNexus(){
    sh "curl -X GET -u admin:admin http://localhost:8081/repository/test-nexus/com/devopsusach2020/DevOpsUsach2020/0.0.1-develop/DevOpsUsach2020-0.0.1-develop.jar -O"
}

def runDownloadedJar(){
    sh "nohup java -jar DevOpsUsach2020-0.0.1-develop.jar &"
    sleep 20
}

def nexusCD(){
    nexusPublisher nexusInstanceId: 'nexus', nexusRepositoryId: 'test-nexus', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: 'jar', filePath: "DevOpsUsach2020-0.0.1-develop.jar"]], mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging: 'jar', version: '1.0.0']]]  
}

return this;