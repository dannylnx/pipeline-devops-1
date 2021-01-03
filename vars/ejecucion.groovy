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
                    script: '''
                        def maven = ['compile','test','jar','runJar','sonar','nexus']
                        def gradle = ['buildAndTest','sonar','runJar','rest','nexus']

                        if (buildtool == 'gradle'){
                            return gradle
                        } else {
                            return maven
                        }
                    ''',
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
