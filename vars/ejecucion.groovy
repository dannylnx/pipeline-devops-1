def call(){
    pipeline {
        agent any
        parameters { 
            choice(name: 'buildtool', choices: ['gradle','maven'], description: 'Elección de herramienta')
            activeChoiceReactiveParam(
                  name: 'stages',
                  description: 'seleccionar stages',
                  referencedParameter: 'buildtool',
                  choiceType: 'RADIO',
                  groovyScript: {
                    script: "if (buildtool == 'gradle'){ return ['compile','test','jar','runJar','sonar','nexus'] } else { return ['buildAndTest','sonar','runJar','rest','nexus'] }",
                    sandbox: true
                  }  
            )
        }
        stages {
            stage('Pipeline') {
                steps {
                    script{
                        figlet params.buildtool
                        stages.split(',').each{
                            try{
                                "${params.buildtool.it}"()
                            }catch (Exception e){
                                error "Error en stage ${it}: ${e}"
                            }
                        }
                    }
                }
            }
        }
    }  
}

return this;

