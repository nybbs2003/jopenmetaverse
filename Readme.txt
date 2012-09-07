
Setting up Git on BitBucket

Add Account Public Key:

https://confluence.atlassian.com/display/BITBUCKET/Using+the+SSH+protocol+with+bitbucket
https://confluence.atlassian.com/display/BITBUCKET/How+to+install+a+public+key+on+your+bitbucket+account

Clone the respository
git clone ssh://git@bitbucket.org/<account Name>/reponame.git


mvn clean -Dtest=TWorkflowTaskClientTest test
mvn clean install -Dmaven.test.skip=true



Some nice Regular expressions

use to replace C# getters to Java getters
public (.*) (.*) \{ get \{ (.*) \} \}


