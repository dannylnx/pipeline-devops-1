
def call(){

    pipeline {
        agent any

        parameters { choice(name: 'buildtool', choices: ['gradle','maven'], description: 'Elección de herramienta de construcción para aplicación covid') }

        stages {
            stage('Hello') {
                steps {
                    script{
                        println 'Herramienta de ejecución seleccionada: ' + params.buildtool
                        def pipe = load "${params.buildtool}.groovy"
                        pipe.call()
                    }
                }
            }
        }
    }  

}

return this;
