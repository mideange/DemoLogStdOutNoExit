DemoLogStdOutNoExit
=============

Occasionally it is useful to call a utility produced by a third party
from within one's own code. However, the third party software might embed
calls like System.out.println(...) and System.exit(int). When using the
third party utility from within one's own project, one might want to prevent
the calls to System.exit(int). Additionally, the usage of System.out or
System.err might be better redirected to the logging framework used
within the calling code.

This simple project demonstrates how to do that easily.

First, a custom java.lang.SecurityManager is built with an implementation
of checkExit(int). It is designed to prevent any calls to System.exit(int).
Any such call within the third party utility will instead throw
NoExitSecurityException to the calling code. That exception can be caught
and handled without exiting the program.  

Second, the checkPermission method of SecurityManager is overriden so that
the setSecurityManager permission is allowed. This is so that when the
third party utility returns, the calling code can restore the original
security manager.

All other calls to NoExitSecurityManager are delegated to the original
SecurityManager.

The other aspect is intercepting the calls to System.out and System.err.
System.out and System.err are both instances of PrintStream. PrintStream
objects are fairly complex, but they can be constructed from OutputStream
objects. To intercept these calls, one can create an OutputStream that
sends data to the logging framework. In this case a class is built on top
of ByteArrayOutputStream called LogOutputStream. It collects data from
the System.out/System.err calls in the third party utility, and then writes
that data to the logging framework.
