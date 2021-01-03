def call(){
    pipeline {
        agent any
        parameters { 
            choice(name: 'buildtool', choices: ['gradle','maven'], description: 'Elección de herramienta')
        }
        properties([
            parameters([
                [$class: 'CascadeChoiceParameter', 
                    choiceType: 'RADIO', 
                    description: 'Stages', 
                    filterLength: 1, 
                    filterable: false, 
                    name: 'stages', 
                    randomName: 'choice-parameter-5631314456178619', 
                    referencedParameters: 'buildtool', 
                    script: [
                        $class: 'GroovyScript', 
                        script: [
                            classpath: [], 
                            sandbox: true, 
                            script: 
                                ''' 
                                    def maven = ['compile','test','jar','runJar','sonar','nexus']
                                    def gradle = ['buildAndTest','sonar','runJar','rest','nexus']

                                    if (buildtool == 'gradle'){
                                        return gradle
                                    } else {
                                        return maven
                                    }
                                '''
                        ]
                    ]
                ]
            ])
        ])
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
