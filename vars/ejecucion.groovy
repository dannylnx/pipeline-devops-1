def call(){

    pipeline {
        agent any

        parameters {
            choice(name: 'buildtool', choices: ['maven','gradle'], description: 'Elección de herramienta de construcción para aplicación covid')
            string(name: 'stages', defaultValue: '' , description: 'Escribir stages a ejecutar en formato: stage1;stage2;stage3. Si stage es vacío, se ejecutarán todos los stages.')
        }

        stages {
            stage('pipeline') {
                steps {
                    script{

                        sh 'env'

                        figlet params.buildtool
                        def archivo = (params.buildtool == 'gradle') ? 'build.gradle' : 'pom.xml'

                        if (fileExists(archivo)){
                            "${params.buildtool}" "${params.stages}"
                        } else {
                            error "archivo ${archivo} no existe. No se puede construir pipeline basado en ${params.buildtool}"
                        }

                    }
                }
            }
        }
        post{
            success{
                slackSend color: 'good', message: "[Grupo4] [${JOB_NAME}] [${BUILD_TAG}] Ejecucion Exitosa", teamDomain: 'dipdevopsusac-tr94431', tokenCredentialId: 'token-slack'
            }
            failure{
                slackSend color: 'danger', message: "[Grupo4] [${env.JOB_NAME}] [${BUILD_TAG}] Ejecucion fallida en stage [${env.TAREA}]", teamDomain: 'dipdevopsusac-tr94431', tokenCredentialId: 'token-slack'
            }
        }
    }
}

return this;
