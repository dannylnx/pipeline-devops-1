package pipeline.git


def checkIfBranchExists(String branch){
	def output = sh (script: "git pull; git ls-remote --heads origin ${branch}", returnStdout: true)
	def respuesta = (!output?.trim()) ? false : true

	return respuesta
}

def isBranchUpdated(String ramaOrigen, String ramaDestino){
	def output = sh (script: "git pull; git log origin/${ramaDestino}..origin/${ramaOrigen}", returnStdout: true)
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
