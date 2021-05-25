# helloworld

package com.hsbc.gbm.amg.fo.jenkins.amgequitylibrary

class Nexus implements Serializable{

    private jenkins

    public Nexus(jenkins){
        this.jenkins = jenkins
    }

    def currentVersion(){
        def matcher = jenkins.readFile('pom.xml') =~ '<version>(.+)</version>'
        matcher ? matcher[0][1] : null
    }

    def releaseVersion(String currentVersion){
        if(currentVersion =~ "-SNAPSHOT"){
            currentVersion.split("-SNAPSHOT")[0]
        } else {
            nextVersion(currentVersion)
        }
    }
	def getNexusVersion(project){
		jenkins.withCredentialsExt({args ->
			def output = jenkins.bat(
				script: "curl -k -s -N -u \"${args[jenkins.gitUser()]}:${args[jenkins.gitPass()]}\" https://dsnexus.uk.hibm.hsbc:8081/nexus/content/repositories/releases/com/hsbc/gbm/amg/fo/Visualiser/${project}/maven-metadata.xml | grep -Po '(?^<=^<version^>)([0-9\\.]+(-SNAPSHOT)?)' | sort --version-sort -r | head -n 1",
				returnStdout: true
			)
			jenkins.echo "### output : ${output} ###"
			def stdoutSplit = output.trim().split("\n")
			def version = stdoutSplit[stdoutSplit.length - 1]
			return version
		})
		
	}

    def getNexusVersionByGroupAndProject(group, project){
		jenkins.withCredentialsExt({args ->
			def output = jenkins.bat(
				script: "curl -k -s -N -u \"${args[jenkins.gitUser()]}:${args[jenkins.gitPass()]}\" https://dsnexus.uk.hibm.hsbc:8081/nexus/content/repositories/releases/com/hsbc/gbm/amg/fo/${group}/${project}/maven-metadata.xml | grep -Po '(?^<=^<version^>)([0-9\\.]+(-SNAPSHOT)?)' | sort --version-sort -r | head -n 1",
				returnStdout: true
			)
			def stdoutSplit = output.trim().split("\n")
			def version = stdoutSplit[stdoutSplit.length - 1]
			return version
		})
	}

    def nextVersion(String releaseVersion){
        def versionSplit = releaseVersion.split("\\.")
        def lastPart = versionSplit[versionSplit.length - 1].toInteger() + 1
        String out = "";
        for (int i = 0; i < versionSplit.length - 1; i++){
            out = out + versionSplit[i] + "."
        }
        out + lastPart.toString()
    }
	
	def publishToNexus(repo, group, artifact, version, file, packaging){
		jenkins.withCredentialsExt({args ->
				jenkins.bat "curl -k -v "+
							"-F \"r=${repo}\" "+
							"-F \"hasPom=false\" "+
							"-F \"g=${group}\" "+
							"-F \"a=${artifact}\" "+
							"-F \"v=${version}\" "+
							"-F \"p=${packaging}\" "+
							"-F \"file=${file}\" "+
							"-u \"${args[jenkins.gitUser()]}:${args[jenkins.gitPass()]}\" "+
							"${Constants.nexusUrl}"
			})
	}

    // Requires a correctly set up pom file (should include scm and distributionManagement tags) at the root of the repository
    def publishToNexus(repoName){
        String currentVersion = currentVersion()
        jenkins.echo "VERSION: ${currentVersion}"
        String releaseVersion = releaseVersion(currentVersion)
        jenkins.echo "RELEASE VERSION: ${releaseVersion}"
        String nextVersion = nextVersion(releaseVersion) + "-SNAPSHOT"
        jenkins.echo "NEXT VERSION: ${nextVersion}"

        jenkins.echo "currentVersion:${currentVersion}, releaseVersion:${releaseVersion}, nextVersion: ${nextVersion}"

        jenkins.withCredentialsExt({args ->
            jenkins.echo "Setting version to ${releaseVersion}"
            jenkins.bat "mvn versions:set -DnewVersion=${releaseVersion}"

            jenkins.echo "Building and Deploying..."
            jenkins.bat "mvn clean deploy"

            jenkins.echo "Committing..."
            jenkins.bat "git checkout ${jenkins.env.BRANCH_NAME}"
            jenkins.bat "mvn -X -Dusername=${args[jenkins.gitUser()]} -Dpassword=${args[jenkins.gitPass()]} scm:checkin scm:tag -Dmessage=\"${Constants.automaticGitCommitTag} releasing ${repoName}:${releaseVersion}\" -Dtag=\"${releaseVersion}\""

            jenkins.stage "SNAPSHOT BUILD"
            jenkins.echo "Setting version to ${nextVersion}"
            jenkins.bat "mvn versions:set -DnewVersion=${nextVersion}"
            jenkins.bat "mvn -Dusername=${args[jenkins.gitUser()]} -Dpassword=${args[jenkins.gitPass()]} scm:checkin -Dmessage=\"${Constants.automaticGitCommitTag} back to snapshot ${repoName}:${nextVersion}\""
        })
    }

    def downloadFromNexus(nexusUrl, targetPath){
        jenkins.withCredentialsExt({args ->
            jenkins.bat "curl -k -L -u ${args[jenkins.gitUser()]}:${args[jenkins.gitPass()]} --create-dirs -o ${targetPath} \"${nexusUrl}\""
        })
    }

}
