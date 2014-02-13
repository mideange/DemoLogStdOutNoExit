/*
Copyright (c) 2014, Cisco Systems
All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted provided that the following conditions are met:

* Redistributions of source code must retain the above copyright notice, this
  list of conditions and the following disclaimer.

* Redistributions in binary form must reproduce the above copyright notice, this
  list of conditions and the following disclaimer in the documentation and/or
  other materials provided with the distribution.

* Neither the name of the {organization} nor the names of its
  contributors may be used to endorse or promote products derived from
  this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package test;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;

import com.cisco.dvbu.ps.*;
import java.io.PrintStream;

public class DemoLogStdOutNoExit {
    static Logger logger = Logger.getLogger(DemoLogStdOutNoExit.class);

    public static void main(String[] args) {
        logger.info("In main");

        TestClass tc = new TestClass();
        SecurityManager sm = System.getSecurityManager();
        PrintStream originalOut = System.out;
        PrintStream originalErr = System.err;
        try {
            Logger testClassLogger = Logger.getLogger(TestClass.class);
            System.setOut(new PrintStream(new LogOutputStream(testClassLogger,Level.INFO)));
            System.setErr(new PrintStream(new LogOutputStream(testClassLogger,Level.ERROR)));
            System.setSecurityManager(new NoExitSecurityManager(sm));
            tc.invoke();
        }
        catch (NoExitSecurityException nese) {
            logger.info("Got the exception from System.exit()");
        }
        finally {
            System.setSecurityManager(sm);
            System.setOut(originalOut);
            System.setErr(originalErr);
        }

        logger.info("Leaving main");
    }
}
