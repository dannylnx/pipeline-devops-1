def call(){

    pipeline {
        agent any

        parameters { 
            choice(name: 'buildtool', choices: ['gradle','maven'], description: 'Elección de herramienta de construcción para aplicación covid')
            string(name: 'stages', defaultValue: '' , description: 'Escribir stages a ejecutar en formato: stage1;stage2;stage3. Si stage es vacío, se ejecutarán todos los stages.') 
        }

        stages {
            stage('Pipeline') {
                steps {
                    script{
                        
                        sh 'env'
                        
                        figlet params.buildtool

                        if (params.buildtool == 'gradle'){
                            gradle "${params.stages}" 
                        } else {
                            maven "${params.stages}"
                        }

                    }
                }
            }
        }
    }  

}

return this;
