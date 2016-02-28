def gitUrl="https://github.com/marcelbirkner/docker-ci-tool-stack"

createDockerJob("docker-admin-version", "sudo /usr/bin/docker version", "")
createDockerJob("docker-admin-list-running-container", "sudo /usr/bin/docker ps", "")
createDockerJob("docker-admin-list-images", "sudo /usr/bin/docker images", "")
createDockerJob("docker-admin-build-jenkins-container", "cd jenkins && sudo /usr/bin/docker build -t jenkins .", gitUrl)
createDockerJob("docker-admin-start-jenkins-container", "sudo /usr/bin/docker run -d --name did_jenkins -p=28080:8080 jenkins", gitUrl)
createDockerJob("docker-admin-stop-jenkins-container", 'sudo /usr/bin/docker stop \$(sudo /usr/bin/docker ps -a -q --filter="name=did_jenkins") && sudo /usr/bin/docker rm \$(sudo /usr/bin/docker ps -a -q --filter="name=did_jenkins")', "")

def createDockerJob(def jobName, def shellCommand, def gitRepository) {

  println "############################################################################################################"
  println "Creating Docker Job ${jobName} for gitRepository=${gitRepository}"
  println "############################################################################################################"

  job(jobName) {
    logRotator {
        numToKeep(10)
    }
    if( "${gitRepository}".size() > 0 ) {
      scm {
        git {
          remote {
            url(gitRepository)
          }
          createTag(false)
          clean()
        }
      }
    }
    steps {
      steps {
        shell(shellCommand)
      }
    }
    publishers {
      chucknorris()
    }
  }
}

listView('admin docker') {
  description('')
  filterBuildQueue()
  filterExecutors()
  jobs {
    regex(/docker-admin-.*/)
  }
  columns {
    buildButton()
    status()
    weather()
    name()
    lastSuccess()
    lastFailure()
    lastDuration()
  }
}
