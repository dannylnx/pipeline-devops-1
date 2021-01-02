def call(){
    pipeline {
        agent any
        
        stages {
            stage('Pipeline') {
                steps {
                    script{
                        figlet params.buildtool
                        stages.split(',').each{
                            try{
                                "${params.buildtool}"."${it}"()
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
