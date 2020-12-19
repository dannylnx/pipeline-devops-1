def call(){
	
	stage('build & test'){
		sh './gradlew clean build'
	}

	stage('sonar'){
	    def sonarhome = tool 'sonar'
        sh "${sonarhome}/bin/sonar-scanner -Dsonar.projectKey=ejemplo-gradle -Dsonar.java.binaries=build"
	}

	stage('run'){
	    sh "nohup bash gradlew bootRun &"
	    sleep 20
	}

	stage('rest'){
	    sh "curl -X GET http://localhost:8082/rest/mscovid/test?msg=testing"
	}

	stage('nexus'){
		nexusPublisher nexusInstanceId: 'nexus', nexusRepositoryId: 'test-nexus', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: 'jar', filePath: 'build/libs/DevOpsUsach2020-0.0.1.jar']], mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging: 'jar', version: '0.0.1']]]
	}

}

return this;