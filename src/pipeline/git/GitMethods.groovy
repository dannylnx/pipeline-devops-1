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
	withCredentials([gitUsernamePassword(credentialsId: 'github-tocken',gitToolName: 'git-tool')]) {
	sh "git push origin --delete ${branch}"
	}
}

def createBranch(String origin, String newBranch){
	withCredentials([gitUsernamePassword(credentialsId: 'github-tocken',gitToolName: 'git-tool')]) {
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

return this;