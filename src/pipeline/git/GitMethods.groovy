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
	sh "git push origin --delete ${branch}"
}

def createBranch(String origin, String newBranch){
	sh '''
		git pull 
		git checkout '''+origin+'''
		git checkout -b '''+newBranch+'''
		git push origin '''+newBranch+'''
	'''
}

return this;