package pipeline.test

def getValidatedStages(String chosenStages, String pipelineStages){

	def stages = []

	if (chosenStages?.trim()){
		chosenStages.split(';').each{
			if (it in pipelineStages){
				stages.add(it)
			} else {
				error "${it} no existe como Stage. Stages disponibles: ${pipelineStages}"
			}
		}
	} else {
		println "Parámetro de stages vacío. Se ejecutarán todos los stages en el siguiente orden: ${pipelineStages}"
		stages = pipelineStages
	}

	return stages
}

def hola(){
	println 'hola'
}

return this;