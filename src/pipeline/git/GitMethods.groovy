package pipeline.git


def checkIfBranchExists(String branch){
	def output = sh (script: "git pull; git ls-remote --heads origin ${branch}", returnStdout: true)
	def respuesta = (!output?.trim()) ? false : true

	return respuesta
}

def isBranchUpdated(String ramaOrigen, String ramaDestino){
	sh "git config --add remote.origin.fetch +refs/heads/${ramaOrigen}:refs/remotes/origin/${ramaOrigen}; git fetch -p"

	def output = sh (script: "git log origin/${ramaDestino}..origin/${ramaOrigen}", returnStdout: true)
	def respuesta = (!output?.trim()) ? true : false

	return respuesta
}

def deleteBranch(String branch){
	sh "git pull; git push origin --delete ${branch}"
}

def createBranch(String branch, String ramaOrigen){
	sh '''
		git reset --hard HEAD
		git pull
		git checkout '''+ramaOrigen+'''
		git checkout -b '''+branch+'''
		git push origin '''+branch+'''
	'''
}

return this;
