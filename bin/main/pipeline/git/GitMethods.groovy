package pipeline.git

def checkIfBranchExists(String branch){
	def output = sh (script: "git ls-remote --heads origin ${branch}", returnStdout: true)
	if (output?.trim()) {
		return true
	} else {
		return false
	}
}

def deleteBranch(String branch){
	withCredentials([gitUsernamePassword(credentialsId: 'jenkins-github-token',gitToolName: 'git-tool')]) {
	sh "git push origin --delete ${branch}"
	}
}

def createBranch(String origin, String newBranch){
	withCredentials([gitUsernamePassword(credentialsId: 'jenkins-github-token',gitToolName: 'git-tool')]) {
	sh '''
		git fetch -p
		git checkout '''+origin+'''; git pull
		git checkout -b '''+newBranch+'''
		git push origin '''+newBranch+'''
		git checkout '''+origin+'''; git pull
		git branch -d '''+newBranch+'''
	'''
	}
}
def createMerge(String origin, String mBranch){
	withCredentials([gitUsernamePassword(credentialsId: 'jenkins-github-token',gitToolName: 'git-tool')]) {
	sh '''
		git fetch -p
		git checkout '''+origin+'''; git pull
		git checkout -b '''+mBranch+'''
		git push origin '''+mBranch+'''
		git checkout '''+origin+'''; git pull
		git branch -d '''+mBranch+'''
	'''
	}
}

return this;